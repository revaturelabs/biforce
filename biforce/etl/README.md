## ETL Team

## Responsibilities

Our Responsibilities consisted of initial cleansing of the data. Importing tables from Caliber Database into Hive filtering only specific columns for the Spark Team. Performing a complex query on the data to join the results into one usuable table. Then using a Hive action to export the final table to HDFS. Also importing all the data that will by OLAP team from Caliber straight to the S3 bucket then to RedShift.

## Goals

1. Create database and table in Hive.

2. Create password alias for Caliber password.

3. Sqoop import specific tables from Caliber into Hive.

4. Create Hive query to join all tables into one workable table for Spark Team.

5. Export Spark table into HDFS.

6. Refer to ETL-Oozie workflow to export Spark data from HDFS to the S3.

7. Spoop import all tables from Caliber into S3 bucket.

8. Export all tables from S3 bucket to RedShift.

## Procedures

1. Create database and Spark table in Hive using commands: 

	```SQL
	CREATE DATABASE IF NOT EXISTS BIFORCE_STAGING;

	use BIFORCE_STAGING;

	CREATE TABLE SPARK_DATA (TEST_TYPE String, SCORE DECIMAL(5, 2), TEST_PERIOD INT, BUILDER_ID INT, GROUP_ID INT, GROUP_TYPE String, BATTERY_ID INT, BATTERY_STATUS String);
	```

2.  Create encrypted password to connect to Caliber. You will be prompted to enter the password when you run the below command: 

	```
	hadoop credential create caliber.password.alias -provider jceks://hdfs/user/root/caliber.password.jceks
	```

3. Run sqoop jobs on locally to import selected Caliber tables into Hive. The connection, username, and password may vary. 

	```
	sqoop import -Dhadoop.security.credential.provider.path=jceks://hdfs/user/root/caliber.password.jceks --connect jdbc:oracle:thin:@caliber-snap.cgbbs6xdwjwh.us-west-2.rds.amazonaws.com:1521/orcl --username caliber --password-alias caliber.password.alias --table CALIBER_TRAINEE --hive-import --hive-table biforce_staging.caliber_trainee --hive-drop-import-delims

	sqoop import -Dhadoop.security.credential.provider.path=jceks://hdfs/user/root/caliber.password.jceks --connect jdbc:oracle:thin:@caliber-snap.cgbbs6xdwjwh.us-west-2.rds.amazonaws.com:1521/orcl --username caliber --password-alias caliber.password.alias --table CALIBER_TRAINER --hive-import --hive-table biforce_staging.caliber_trainer --hive-drop-import-delims

	sqoop import -Dhadoop.security.credential.provider.path=jceks://hdfs/user/root/caliber.password.jceks --connect jdbc:oracle:thin:@caliber-snap.cgbbs6xdwjwh.us-west-2.rds.amazonaws.com:1521/orcl --username caliber --password-alias caliber.password.alias --table CALIBER_BATCH --hive-import --hive-table biforce_staging.caliber_batch --hive-drop-import-delims

	sqoop import -Dhadoop.security.credential.provider.path=jceks://hdfs/user/root/caliber.password.jceks --connect jdbc:oracle:thin:@caliber-snap.cgbbs6xdwjwh.us-west-2.rds.amazonaws.com:1521/orcl --username caliber --password-alias caliber.password.alias --table CALIBER_NOTE --hive-import --hive-table biforce_staging.caliber_note --hive-drop-import-delims

	sqoop import -Dhadoop.security.credential.provider.path=jceks://hdfs/user/root/caliber.password.jceks --connect jdbc:oracle:thin:@caliber-snap.cgbbs6xdwjwh.us-west-2.rds.amazonaws.com:1521/orcl --username caliber --password-alias caliber.password.alias --table CALIBER_GRADE --hive-import --hive-table biforce_staging.caliber_grade --hive-drop-import-delims

	sqoop import -Dhadoop.security.credential.provider.path=jceks://hdfs/user/root/caliber.password.jceks --connect jdbc:oracle:thin:@caliber-snap.cgbbs6xdwjwh.us-west-2.rds.amazonaws.com:1521/orcl --username caliber --password-alias caliber.password.alias --table CALIBER_ASSESSMENT --hive-import --hive-table biforce_staging.caliber_assessment --hive-drop-import-delims

	sqoop import -Dhadoop.security.credential.provider.path=jceks://hdfs/user/root/caliber.password.jceks --connect jdbc:oracle:thin:@caliber-snap.cgbbs6xdwjwh.us-west-2.rds.amazonaws.com:1521/orcl --username caliber --password-alias caliber.password.alias --table CALIBER_CATEGORY --hive-import --hive-table biforce_staging.caliber_category --hive-drop-import-delims

	sqoop import -Dhadoop.security.credential.provider.path=jceks://hdfs/user/root/caliber.password.jceks --connect jdbc:oracle:thin:@caliber-snap.cgbbs6xdwjwh.us-west-2.rds.amazonaws.com:1521/orcl --username caliber --password-alias caliber.password.alias --table CALIBER_ADDRESS --hive-import --hive-table biforce_staging.caliber_address --hive-drop-import-delims
	```

4. Create a Hive query to join all tables into one workable table for Spark Team.
	
	```SQL
	INSERT OVERWRITE TABLE SPARK_DATA 
	SELECT
		CALIBER_ASSESSMENT.ASSESSMENT_TYPE AS TYPE,
		CALIBER_GRADE.SCORE,
		CALIBER_ASSESSMENT.WEEK_NUMBER,
		CALIBER_BATCH.TRAINER_ID,
		CALIBER_BATCH.BATCH_ID,
		CALIBER_BATCH.SKILL_TYPE, 
		CALIBER_GRADE.TRAINEE_ID,
		CALIBER_TRAINEE.TRAINING_STATUS
	FROM CALIBER_ASSESSMENT, CALIBER_BATCH, CALIBER_GRADE, CALIBER_TRAINEE
	WHERE CALIBER_ASSESSMENT.ASSESSMENT_ID = CALIBER_GRADE.ASSESSMENT_ID
	AND CALIBER_ASSESSMENT.BATCH_ID = CALIBER_BATCH.BATCH_ID
	AND CALIBER_GRADE.TRAINEE_ID = CALIBER_TRAINEE.TRAINEE_ID

	UNION ALL

	SELECT
		CALIBER_NOTE.NOTE_TYPE AS TYPE,
		CASE
			WHEN UPPER(CALIBER_NOTE.QC_STATUS) = 'POOR' THEN 65 
			WHEN UPPER(CALIBER_NOTE.QC_STATUS) = 'AVERAGE' THEN 75
			WHEN UPPER(CALIBER_NOTE.QC_STATUS) = 'GOOD' THEN 85
			WHEN UPPER(CALIBER_NOTE.QC_STATUS) = 'SUPERSTAR' THEN 95
			ELSE 0 END as SCORE,
		CALIBER_NOTE.WEEK_NUMBER,
		CALIBER_BATCH.TRAINER_ID,
		CALIBER_BATCH.BATCH_ID,
		CALIBER_BATCH.SKILL_TYPE,
		CALIBER_TRAINEE.TRAINEE_ID,
		CALIBER_TRAINEE.TRAINING_STATUS
	FROM CALIBER_NOTE, CALIBER_BATCH, CALIBER_TRAINEE
	WHERE CALIBER_NOTE.TRAINEE_ID = CALIBER_TRAINEE.TRAINEE_ID
	AND CALIBER_BATCH.BATCH_ID = CALIBER_TRAINEE.BATCH_ID
	AND CALIBER_NOTE.QC_STATUS IS NOT NULL;
	```

5. Run command below in Hive to export the Spark table to HDFS for Spark team to use. Note the directory path.

	```
	insert overwrite directory 'user/hadoop/biforce/Spark_Data' row format delimited fields terminated by ',' select * from spark_data; 
	```

6. Refer to ETL-Oozie workflow to pull Spark table into S3.

7. Spoop import all tables from Caliber into S3 bucket. Connection and username may differ. Change value inside * * respectively. Use this command for each table in Caliber that the OLAP team would like to use for analysis.

	```
	sqoop import -Dhadoop.security.credential.provider.path=jceks://hdfs/user/root/caliber.password.jceks -Dfs.s3a.access.key=*accesskey* -Dfs.s3a.secret.key=*secretkey* --connect jdbc:oracle:thin:@caliber-snap.cgbbs6xdwjwh.us-west-2.rds.amazonaws.com:1521/orcl --username caliber --password-alias caliber.password.alias --table *desired table* --target-dir s3a://*revature bucket*/*target directory* --incremental append --check-column *checked column* --temporary-rootdir s3a://*revature bucket*/*temporary directory* -m 1
	```

8. Export all tables from S3 bucket to RedShift.

## Results


