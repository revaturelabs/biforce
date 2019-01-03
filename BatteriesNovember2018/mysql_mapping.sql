battery_group_relationship 
    battery_group => batch_id
    battery_id => trainee_id 

battery_test => caliber_assessments
    battery_test_id => assesment_id
    raw_score => raw_score
    battery_test_type => assesment_type
    test_period => week_number
    battery_group => batch_id
    test_category => accessment_category

battery_test_grade => caliber_grade
    voltage_id => grade_id
    date_received => date_received
    score => scorce
    battery_test_id => assement_id
    battery_id => trainee_id

battery_note => caliber_note
    note_id => note_id
    max_visibility => max_visibility
    is_AAA_feedback => is_qc_feedback
    AAA_status => qc_status
    note_type => note_type
    test_period => week_number
    battery_group => batch_id
    battery_id => trainee_id 

battery_status => caliber_trainee
    battery_id => trainee_id
    battery_status => batch_id

scoops 

sqoop import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table battery_group_relationship -m 1
sqoop import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table battery_test -m 1
sqoop import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table battery_test_grade -m 1
sqoop import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table battery_note -m 1
sqoop import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table battery_status -m 1 


SELECT /*csv*/ 
TRAINEE_ID battery_id, 
BATCH_ID battery_group
FROM CALIBER_TRAINEE;

SELECT /*csv*/
ASSESSMENT_ID battery_test_id,
raw_score,
CASE 
    WHEN ASSESSMENT_TYPE = 'Verbal' 
    THEN 'Visual'
    WHEN ASSESSMENT_TYPE = 'Exam' 
    THEN 'Voltmeter'
    WHEN ASSESSMENT_TYPE = 'Project' 
    THEN 'Vibration'
    WHEN ASSESSMENT_TYPE = 'Other' 
    THEN 'Drop'
END AS battery_test_type,
week_number test_period,
batch_id battery_group,
assessment_category test_category
FROM CALIBER_ASSESSMENT;

SELECT /*csv*/
grade_id voltage_id,
date_received,
score,
ASSESSMENT_ID battery_test_id,
trainee_id battery_id
FROM CALIBER_GRADE;

SELECT 
NOTE_ID,
max_visibility,
is_qc_feedback is_AAA_feedback,

CASE
WHEN UPPER(QC_STATUS) = 'UNDEFINED' THEN '0'
WHEN UPPER(QC_STATUS) = 'POOR' THEN '25'
WHEN UPPER(QC_STATUS) = 'AVERAGE' THEN '50'
WHEN UPPER(QC_STATUS) = 'GOOD' THEN '75'
WHEN UPPER(QC_STATUS) = 'SUPERSTAR' THEN '100'
END AS AAA_status,
CASE 
    WHEN NOTE_TYPE = 'QC_BATCH' THEN 'VOCAL'
    WHEN NOTE_TYPE = 'TRAINEE' THEN 'MEMO'
    WHEN NOTE_TYPE = 'QC_TRAINEE' THEN 'LETTER'
    WHEN NOTE_TYPE = 'BATCH' THEN 'PAMPHLET'
END AS NOTE_TYPE,
week_number test_period,
batch_id battery_group,
trainee_id battery_id
FROM CALIBER_NOTE;
