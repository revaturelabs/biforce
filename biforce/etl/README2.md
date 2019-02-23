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
	CREATE DATABASE BIFORCE_STAGING;

	use BIFORCE_STAGING;

	CREATE TABLE SPARK_DATA (SURROGATE_KEY INT NOT NULL AUTO_INCREMENT, TEST_TYPE INT, SCORE DECIMAL(5, 2), TEST_PERIOD INT, BUILDER_ID INT, GROUP_ID INT, GROUP_TYPE INT, BATTERY_ID INT, BATTERY_STATUS INT, PRIMARY KEY(SURROGATE_KEY));

	CREATE TABLE CALIBER_TRAINEE (TRAINEE_ID INT, TRAINEE_EMAIL varchar(20), TRAINEE_NAME varchar(20), TRAINING_STATUS VARCHAR(20), BATCH_ID INT, PHONE_NUMBER varchar(20), PROFILE_URL varchar(20), SKYPE_ID varchar(20), RESOURCE_ID varchar(20), FLAG_NOTES varchar(40), FLAG_STATUS varchar(20), TECH_SCREEN_SCORE INT, RECRUITER_NAME varchar(20), COLLEGE varchar(20), DEGREE varchar(20), MAJOR varchar(20), TECH_SCREENER_NAME varchar(20), REVPRO_PROJECT_COMPLETION INT, INDEX(TRAINEE_ID, BATCH_ID));

	CREATE TABLE CALIBER_BATCH (BATCH_ID INT, BORDERLINE_GRADE_THRESHOLD INT, END_DATE DATE, GOOD_GRADE_THRESHOLD INT, LOCATION varchar(20), SKILL_TYPE VARCHAR(20), START_DATE DATE, TRAINING_NAME varchar(20), TRAINING_TYPE varchar(20), NUMBER_OF_WEEKS INT, CO_TRAINER_ID INT, TRAINER_ID INT, RESOURCE_ID varchar(20), ADDRESS_ID INT, GRADED_WEEKS INT, INDEX(BATCH_ID));

	CREATE TABLE CALIBER_NOTE (NOTE_ID INT, NOTE_CONTENT varchar(400), MAX_VISIBILITY INT, IS_QC_FEEDBACK INT, QC_STATUS VARCHAR(20), NOTE_TYPE VARCHAR(20), WEEK_NUMBER INT, BATCH_ID INT, TRAINEE_ID INT, INDEX(QC_STATUS, TRAINEE_ID));

	CREATE TABLE CALIBER_GRADE (GRADE_ID INT, DATE_RECEIVED TIMESTAMP, SCORE DECIMAL(5, 2), ASSESSMENT_ID INT, TRAINEE_ID INT, INDEX(ASSESSMENT_ID, TRAINEE_ID));

	CREATE TABLE CALIBER_ASSESSMENT (ASSESSMENT_ID INT, RAW_SCORE INT, ASSESSMENT_TITLE varchar(20), ASSESSMENT_TYPE VARCHAR(20), WEEK_NUMBER INT, BATCH_ID INT, ASSESSMENT_CATEGORY INT, INDEX(ASSESSMENT_ID, BATCH_ID));
	```
	
2. Create Sqoop job on the machine that will perform Oozie. Connect, username, and password will vary:
	
	```SQL
	sqoop job \ 
	--create spark_data_join \
	-- eval \
	--connect jdbc:mysql://sandbox-hdp.hortonworks.com/BIFORCE_STAGING \
	--username root \
	--password ? \
	-e "INSERT INTO SPARK_DATA (TEST_TYPE, SCORE, TEST_PERIOD, BUILDER_ID, GROUP_ID, GROUP_TYPE, BATTERY_ID, BATTERY_STATUS)
	SELECT
		A.ASSESSMENT_TYPE,
		G.SCORE,
		A.WEEK_NUMBER,
		B.TRAINER_ID,
		B.BATCH_ID,
		B.SKILL_TYPE, 
		G.TRAINEE_ID,
		T.TRAINING_STATUS
	FROM CALIBER_ASSESSMENT A, CALIBER_BATCH B, CALIBER_GRADE G, CALIBER_TRAINEE T
	WHERE A.ASSESSMENT_ID = G.ASSESSMENT_ID
	AND A.BATCH_ID = B.BATCH_ID
	AND G.TRAINEE_ID = T.TRAINEE_ID

	UNION ALL

	SELECT 
		N.NOTE_TYPE
		N.QC_STATUS
		N.WEEK_NUMBER,
		B.TRAINER_ID,
		B.BATCH_ID,
		B.SKILL_TYPE
		T.TRAINEE_ID,
		T.TRAINING_STATUS
	FROM CALIBER_TRAINEE T, CALIBER_NOTE N, CALIBER_BATCH B
	WHERE N.TRAINEE_ID = T.TRAINEE_ID
	AND B.BATCH_ID = T.BATCH_ID
	AND N.QC_STATUS IS NOT NULL;"

3. Create encrypted password to connect to Caliber. You will be prompted to enter the password when you run the below command: 

```
	hadoop credential create mydb.password.alias -provider jceks://hdfs/user/root/mysql.password.jceks
```
	
4. Refer to Oozie workflow for automated Sqoop commands. Remember the above commands must be completed before the Oozie workflow will work!

## Results

We were able to successfully run all the Sqoop tasks through Oozie using Hortonworks. One of the major drawbacks was processing the initial complex query on MySQL but this issue was resolved by adding INDEXs on the columns that were used in the WHERE clause. 
