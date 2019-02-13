## ETL Team

## Responsibilities

Our Responsibilities consisted of initial cleansing of the data through Sqoop importing tables from Caliber Database into HDFS filtering only specific columns, Sqoop exporting the data into MySQL, performing complex queries on the tables to join the results into one usuable result, then Sqoop importing the final table back in HDFS. This entire process also needed to be automated in Oozie.

## Goals

1. Because we didn't have access to Caliber, we needed immediate access to test tables which was provided to us through our team leads.

2. Create Tables in MySQL defining a workable schema to work with each data type.

3. Create Sqoop commands for all export and import commands.

4. Create MySQL query to join all tables into one workable table.

5. Test all Sqoop commands on local machine.

6. Cordinate with Oozie team to switch Sqoop jobs over into Oozie actions.

7. Figure out how to encrypt passwords to implement Sqoop commands to import from Caliber.

8. Add Caliber Sqoop imports into Oozie.

## Procedures

1. Create Database and Tables in MySQL database using below commands: 

	```SQL
	CREATE DATABASE BATTERY_STAGING;

	use BATTERY_STAGING;

	CREATE TABLE BATTERY_TEST (SURROGATE_KEY INT NOT NULL AUTO_INCREMENT, TEST_TYPE INT, SCORE DECIMAL(5, 2), TEST_PERIOD INT, BUILDER_ID INT, GROUP_ID INT, GROUP_TYPE INT, BATTERY_ID INT, BATTERY_STATUS INT, PRIMARY KEY(SURROGATE_KEY));

	CREATE TABLE BATTERY (TRAINEE_ID INT, TRAINING_STATUS VARCHAR(20), BATCH_ID INT, INDEX(TRAINEE_ID, BATCH_ID));

	CREATE TABLE BATTERY_GROUP (BATCH_ID INT, SKILL_TYPE VARCHAR(20), TRAINER_ID INT, INDEX(BATCH_ID));

	CREATE TABLE BATTERY_QUALITATIVE (NOTE_ID INT, QC_STATUS VARCHAR(20), NOTE_TYPE VARCHAR(20), WEEK_NUMBER INT, BATCH_ID INT, TRAINEE_ID INT, INDEX(QC_STATUS, TRAINEE_ID));

	CREATE TABLE BATTERY_QUANTITATIVE (GRADE_ID INT, SCORE DECIMAL(5, 2), ASSESSMENT_ID INT, TRAINEE_ID INT, INDEX(ASSESSMENT_ID, TRAINEE_ID));

	CREATE TABLE BATTERY_ASSESSMENT (ASSESSMENT_ID INT, ASSESSMENT_TYPE VARCHAR(20), WEEK_NUMBER INT, BATCH_ID INT, INDEX(ASSESSMENT_ID, BATCH_ID));
	```
	
2. Create Sqoop job on the machine that will perform Oozie. Connect, username, and password will vary:
	
	```SQL
	sqoop job \
--create battery_test_join \
-- eval \
--connect jdbc:mysql://sandbox-hdp.hortonworks.com/BATTERY_STAGING \
--username root \
--password mySQLPW \
-e "INSERT INTO BATTERY_TEST (TEST_TYPE, SCORE, TEST_PERIOD, BUILDER_ID, GROUP_ID, GROUP_TYPE, BATTERY_ID, BATTERY_STATUS)
	SELECT
		CASE 
			WHEN UPPER(A.ASSESSMENT_TYPE) = 'VERBAL' THEN 1 
			WHEN UPPER(A.ASSESSMENT_TYPE) = 'EXAM' THEN 2
			WHEN UPPER(A.ASSESSMENT_TYPE) = 'PROJECT' THEN 3
			WHEN UPPER(A.ASSESSMENT_TYPE) = 'OTHER' THEN 4
			ELSE 0
		END,
			Q.SCORE,
		A.WEEK_NUMBER,
		G.TRAINER_ID,
		G.BATCH_ID,
		CASE 
			WHEN UPPER(G.SKILL_TYPE) = 'SDET' THEN 1 
			WHEN UPPER(G.SKILL_TYPE) = 'J2EE' THEN 2
			WHEN UPPER(G.SKILL_TYPE) = 'OTHER' THEN 3
			WHEN UPPER(G.SKILL_TYPE) = 'BPM' THEN 4
			WHEN UPPER(G.SKILL_TYPE) = 'NET' THEN 5
			WHEN UPPER(G.SKILL_TYPE) = 'BPM' THEN 6
			WHEN UPPER(G.SKILL_TYPE) = 'MICROSERVICES' THEN 7
			ELSE 0
		END,
		Q.TRAINEE_ID,
		CASE
			WHEN UPPER(B.TRAINING_STATUS) = 'DROPPED' THEN 0
			WHEN UPPER(B.TRAINING_STATUS) = 'EMPLOYED' THEN 1
			WHEN UPPER(B.TRAINING_STATUS) = 'TRAINING' THEN 2
			WHEN UPPER(B.TRAINING_STATUS) = 'SIGNED' THEN 3
			WHEN UPPER(B.TRAINING_STATUS) = 'CONFIRMED' THEN 4
			WHEN UPPER(B.TRAINING_STATUS) = 'MARKETING' THEN 5
			ELSE 6
		END
	FROM BATTERY_ASSESSMENT A, BATTERY_GROUP G, BATTERY_QUANTITATIVE Q, BATTERY B
	WHERE A.ASSESSMENT_ID = Q.ASSESSMENT_ID
	AND A.BATCH_ID = G.BATCH_ID
	AND Q.TRAINEE_ID = B.TRAINEE_ID

	UNION ALL

	SELECT 
		CASE 
			WHEN UPPER(Q.NOTE_TYPE) = 'TRAINEE' THEN 5 
			WHEN UPPER(Q.NOTE_TYPE) = 'QC_TRAINEE' THEN 6
			WHEN UPPER(Q.NOTE_TYPE) = 'BATCH' THEN 7
			WHEN UPPER(Q.NOTE_TYPE) = 'QC_BATCH' THEN 8
			ELSE 0
		END,
		CASE 
			WHEN UPPER(Q.QC_STATUS) = 'POOR' THEN 65 
			WHEN UPPER(Q.QC_STATUS) = 'AVERAGE' THEN 75
			WHEN UPPER(Q.QC_STATUS) = 'GOOD' THEN 85
			WHEN UPPER(Q.QC_STATUS) = 'SUPERSTAR' THEN 95
			ELSE 0
		END,
		Q.WEEK_NUMBER,
		G.TRAINER_ID,
		G.BATCH_ID,
		CASE 
			WHEN UPPER(G.SKILL_TYPE) = 'SDET' THEN 1 
			WHEN UPPER(G.SKILL_TYPE) = 'J2EE' THEN 2
			WHEN UPPER(G.SKILL_TYPE) = 'OTHER' THEN 3
			WHEN UPPER(G.SKILL_TYPE) = 'BPM' THEN 4
			WHEN UPPER(G.SKILL_TYPE) = 'NET' THEN 5
			WHEN UPPER(G.SKILL_TYPE) = 'BPM' THEN 6
			WHEN UPPER(G.SKILL_TYPE) = 'MICROSERVICES' THEN 7
			ELSE 0
		END,
		B.TRAINEE_ID,
		CASE
			WHEN UPPER(B.TRAINING_STATUS) = 'DROPPED' THEN 0
			WHEN UPPER(B.TRAINING_STATUS) = 'EMPLOYED' THEN 1
			WHEN UPPER(B.TRAINING_STATUS) = 'TRAINING' THEN 2
			WHEN UPPER(B.TRAINING_STATUS) = 'SIGNED' THEN 3
			WHEN UPPER(B.TRAINING_STATUS) = 'CONFIRMED' THEN 4
			WHEN UPPER(B.TRAINING_STATUS) = 'MARKETING' THEN 5
			ELSE 6
		END
	FROM BATTERY B, BATTERY_QUALITATIVE Q, BATTERY_GROUP G
	WHERE Q.TRAINEE_ID = B.TRAINEE_ID
	AND G.BATCH_ID = B.BATCH_ID
	AND Q.QC_STATUS IS NOT NULL;"

3. Create encrypted password to connect to Caliber. You will be prompted to enter the password when you run the below command: 

```
	hadoop credential create mydb.password.alias -provider jceks://hdfs/user/root/mysql.password.jceks
```
	
4. Refer to Oozie workflow for automated Sqoop commands. Remember the above commands must be completed before the Oozie workflow will work!

## Results

We were able to successfully run all the Sqoop tasks through Oozie using Hortonworks. One of the major drawbacks was processing the initial complex query on MySQL but this issue was resolved by adding INDEXs on the columns that were used in the WHERE clause. 