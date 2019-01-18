
--BiForce sample SQL query.
--  Purpose: See how much of the business question you can answer with
--  a SQL query. 
WITH 
--Initail filtering to what status types to have in output.
--  0 - dropped
--  1 - employeed
--  2 - training
--  3 - signed
--  4 - confirmed
--  5 - marketed
--  6 - unknown
--  Useful if only intertested in a group of batteries.  
FILTERED_BATTERIES AS
(SELECT * FROM BATTERY_TEST
WHERE BATTERY_STATUS IN (2)),


--Flatten the table to just distinct values
--  Removes all the test values for each battery
--  Adds a column for condition and message. Both set to null.
--      Condition is the output of this query.
--      Message is the collected messages from the queries.
BATTERIES_FILTERED_DISTINCT AS
(SELECT 
    DISTINCT BATTERY_ID,
    GROUP_ID,
    GROUP_TYPE_ID,
    BUILDER_ID,
    BATTERY_STATUS,
    NULL CONDITION,
    NULL MESSAGE
FROM FILTERED_BATTERIES),

--**************************************************
--First Metric: Number of tests Missed
--**************************************************

--Count how many test the batteries have for each 
--  group.  Used to calculate if a battery is missing
--  a test.
M1_GROUP_BATTERY_COUNT AS(
SELECT GROUP_ID, BATTERY_ID, COUNT(TEST_SCORE) BATTERY_TEST_COUNT
FROM FILTERED_BATTERIES
WHERE TEST_PERIOD IN (1, 2, 3)
GROUP BY GROUP_ID, BATTERY_ID),

--Find the max test count for each battery.
M1_GROUP_TEST_MAX_COUNT AS(
SELECT GROUP_ID, MAX(BATTERY_TEST_COUNT) MAX_GROUP_TEST_COUNT
FROM M1_GROUP_BATTERY_COUNT
GROUP BY GROUP_ID),

--Find the batteries with missing one test
--  need further metrics to refine this group.
M1_MISSING_ONE_TEST AS (
SELECT 
    C.BATTERY_ID,
    A.GROUP_ID,
    GROUP_TYPE_ID,
    BUILDER_ID,
    BATTERY_STATUS,
    'YELLOW' CONDITION,
    'Missing one test in first three weeks(50% passing)' MESSAGE
FROM M1_GROUP_TEST_MAX_COUNT A,
    M1_GROUP_BATTERY_COUNT B,
    BATTERIES_FILTERED_DISTINCT C
WHERE A.GROUP_ID = B.GROUP_ID
AND B.BATTERY_ID = C.BATTERY_ID
AND A.MAX_GROUP_TEST_COUNT - B.BATTERY_TEST_COUNT = 1),

--Find batteries that miss two or more tests
--  no further metrics need for this group.
M1_MISSING_TWO_PLUS_TESTS AS (
SELECT 
    C.BATTERY_ID,
    A.GROUP_ID,
    GROUP_TYPE_ID,
    BUILDER_ID,
    BATTERY_STATUS,
    'RED' CONDITION,
    'Missing ' || TO_CHAR(A.MAX_GROUP_TEST_COUNT - B.BATTERY_TEST_COUNT) ||' test in first three weeks(0% passing)' MESSAGE
FROM M1_GROUP_TEST_MAX_COUNT A,
    M1_GROUP_BATTERY_COUNT B,
    BATTERIES_FILTERED_DISTINCT C
WHERE A.GROUP_ID = B.GROUP_ID
AND B.BATTERY_ID = C.BATTERY_ID
AND A.MAX_GROUP_TEST_COUNT - B.BATTERY_TEST_COUNT > 1),

--Metric 1 final output
--  union both the missing one test and missing two plus tests.  
M1_RESULTS AS(
SELECT * FROM M1_MISSING_ONE_TEST
UNION ALL
SELECT * FROM M1_MISSING_TWO_PLUS_TESTS),

--*******************************************************
--Metric #2(Has done atleast one project in three weeks)
--*******************************************************

--Remove out the batteries from Metric 1
M2_START AS(
SELECT 
    BATTERY_ID,
    GROUP_ID,
    GROUP_TYPE_ID,
    BUILDER_ID,
    BATTERY_STATUS,
    NULL CONDITION,
    'No Missing Test in first three weeks' MESSAGE
FROM BATTERIES_FILTERED_DISTINCT
WHERE BATTERY_ID NOT IN
   -- Should rework to use a left join on battery_id, where 
    -- condition is equall to null.
(SELECT BATTERY_ID FROM M1_RESULTS)),

--look for battiers with atleast one project in the first three weeks
--  give average if more than one project
--  TEST_TYPE = 3 = PROJECT
M2_HAS_PROJECT AS(
SELECT BATTERY_ID, AVG(TEST_SCORE) PROJECT_AVG
FROM FILTERED_BATTERIES
WHERE TEST_TYPE = 3
AND TEST_PERIOD IN (1, 2, 3)
GROUP BY BATTERY_ID),

--Projects with 100% score
-- Found 16 project 100%
--  14 were passing
--  2 were failing
--  87% chance of passing with 
M2_PROJECT_100_AVG AS(
SELECT
    A.BATTERY_ID,
    GROUP_ID,
    GROUP_TYPE_ID,
    BUILDER_ID,
    BATTERY_STATUS,
    'GREEN' CONDITION,
    MESSAGE || ', Has project score = 100 (88% passing)' MESSAGE
FROM M2_START A,  M2_HAS_PROJECT B
WHERE A.BATTERY_ID = B.BATTERY_ID
AND PROJECT_AVG = 100),

--Projects below 50% score
--  all dropped
M2_PROJECT_LESS_50 AS(
SELECT
    A.BATTERY_ID,
    GROUP_ID,
    GROUP_TYPE_ID,
    BUILDER_ID,
    BATTERY_STATUS,
    'RED' CONDITION,
    MESSAGE || ', Has project score > ' ||
    TO_CHAR(PROJECT_AVG)||
    ' (0% passing)' MESSAGE
FROM M2_START A,  M2_HAS_PROJECT B
WHERE A.BATTERY_ID = B.BATTERY_ID
AND PROJECT_AVG < 50
ORDER BY PROJECT_AVG),

--Projects above 50 but below 100 score
--  12 failed
--  8 passed
--  40% chance passing
--  NEEDS more meterics to refine.
M2_PROJECT_50_TO_99 AS(
SELECT
    A.BATTERY_ID,
    GROUP_ID,
    GROUP_TYPE_ID,
    BUILDER_ID,
    BATTERY_STATUS,
    'YELLOW' CONDITION,
    MESSAGE || ', Has project score > ' ||
    TO_CHAR(PROJECT_AVG)||
    ' (40% passing)' MESSAGE
FROM M2_START A,  M2_HAS_PROJECT B
WHERE A.BATTERY_ID = B.BATTERY_ID
AND PROJECT_AVG >= 50
AND PROJECT_AVG < 100
ORDER BY PROJECT_AVG),

--M2 Results
M2_RESULTS AS (
SELECT * FROM M2_PROJECT_100_AVG
UNION ALL 
SELECT * FROM M2_PROJECT_LESS_50
UNION ALL 
SELECT * FROM M2_PROJECT_50_TO_99),


--*****************************************************
--Metric#3 Exam scores
--*****************************************************

--Remove batteries from metric 2
M3_START AS(
SELECT 
    BATTERY_ID,
    GROUP_ID,
    GROUP_TYPE_ID,
    BUILDER_ID,
    BATTERY_STATUS,
    NULL CONDITION,
    MESSAGE || ', No Project' MESSAGE
FROM M2_START
WHERE BATTERY_ID NOT IN
(SELECT BATTERY_ID FROM M2_RESULTS)),

--Average weekly exam scores per battery.
M3_EXAM_WEEKLY_AVG AS(
SELECT 
    A.BATTERY_ID, 
    TEST_PERIOD, 
    AVG(TEST_SCORE) BATTERY_WEEK_AVG
FROM FILTERED_BATTERIES A, M3_START B
WHERE A.BATTERY_ID = B.BATTERY_ID
AND TEST_TYPE = 2
AND TEST_PERIOD IN (1, 2, 3)
GROUP BY A.BATTERY_ID, TEST_PERIOD),

-- Compair only batteries with three weeks
-- of verbal scores.
-- WEEK_EXAM_COUNT >= 3 if more weeks are added in future.
M3_HAS_THREE_WEEKS_OF_EXAMS AS(
SELECT BATTERY_ID, COUNT(TEST_PERIOD) WEEK_EXAM_COUNT
FROM M3_EXAM_WEEKLY_AVG
GROUP BY BATTERY_ID
HAVING COUNT(TEST_PERIOD) = 3),

-- Find weekly average for group type
-- Using M2 start because it eliminates 
-- batteries with missing tests.  
M3_GROUP_TYPE_ID_WEEKLY_AVG AS (
SELECT 
    GROUP_TYPE_ID, TEST_PERIOD, AVG(TEST_SCORE) GROUP_TYPE_WEEKLY_AVG
FROM BATTERY_TEST
WHERE GROUP_TYPE_ID IN
    (SELECT GROUP_TYPE_ID FROM M2_START)
AND TEST_PERIOD IN (1, 2, 3)
GROUP BY GROUP_TYPE_ID, TEST_PERIOD),

--Get the average difference between
-- the group_type weekly average and
-- the battery group average.  
M3_EXAM_DIFF AS(
SELECT
    A.BATTERY_ID,
    AVG(B.BATTERY_WEEK_AVG - C.GROUP_TYPE_WEEKLY_AVG) AVG_DIFF
FROM M3_START A, M3_EXAM_WEEKLY_AVG B, M3_GROUP_TYPE_ID_WEEKLY_AVG C
WHERE A.BATTERY_ID = B.BATTERY_ID
AND A.GROUP_TYPE_ID = C.GROUP_TYPE_ID
AND A.BATTERY_ID IN 
    (SELECT BATTERY_ID FROM M3_HAS_THREE_WEEKS_OF_EXAMS)
GROUP BY A.BATTERY_ID
ORDER BY AVG_DIFF),

--If diff > -20 then Green
-- 29 passing
-- 7 failing
-- 80% passing
-- NEEDS more metrics to figure out.
M3_DIFF_PERCENT_ABOVE_NEG_20 AS(
SELECT 
    A.BATTERY_ID,
    GROUP_ID,
    GROUP_TYPE_ID,
    BUILDER_ID,
    BATTERY_STATUS,
    'GREEN' CONDITION,
    MESSAGE || 'Exam avg diff Above -20%, avg-diff = ' ||
    TO_CHAR(AVG_DIFF, 99.99) ||
    '(80% passing)' MESSAGE
FROM M3_START A, M3_EXAM_DIFF B
WHERE A.BATTERY_ID = B.BATTERY_ID
AND AVG_DIFF > -20
ORDER BY AVG_DIFF),

M3_DIFF_PERCENT_BELOW_NEG_20 AS(
SELECT 
    A.BATTERY_ID,
    GROUP_ID,
    GROUP_TYPE_ID,
    BUILDER_ID,
    BATTERY_STATUS,
    'RED' CONDITION,
    MESSAGE || 'Exam avg diff below -20%, avg-diff = ' ||
    TO_CHAR(AVG_DIFF, 99.99) ||
    '(0% passing)' MESSAGE
FROM M3_START A, M3_EXAM_DIFF B
WHERE A.BATTERY_ID = B.BATTERY_ID
AND AVG_DIFF <= -20
ORDER BY AVG_DIFF),

--M2 Results
M3_RESULTS AS (
SELECT * FROM M3_DIFF_PERCENT_ABOVE_NEG_20
UNION ALL 
SELECT * FROM M3_DIFF_PERCENT_BELOW_NEG_20),


--*****************************************************
-- No meteric bucket
--****************************************************
-- 13 Passing
-- 15 failing
-- 46% passing
NOT_IN_M1_M2_M3 AS (
SELECT 
    BATTERY_ID,
    GROUP_ID,
    GROUP_TYPE_ID,
    BUILDER_ID,
    BATTERY_STATUS,
    'YELLOW' CONDITION,
    'no missing tests but not three weeks of exams, not bucketed (46% passing)' MESSAGE
FROM M3_START
WHERE BATTERY_ID NOT IN 
(SELECT BATTERY_ID FROM M3_RESULTS)),

--************************************************************
-- End result
--************************************************************
FINAL_RESULT AS (
SELECT * FROM M1_RESULTS
UNION ALL
SELECT * FROM M2_RESULTS
UNION ALL
SELECT * FROM M3_RESULTS
UNION ALL
SELECT * FROM NOT_IN_M1_M2_M3)

SELECT * FROM FINAL_RESULT
ORDER BY BATTERY_ID;
