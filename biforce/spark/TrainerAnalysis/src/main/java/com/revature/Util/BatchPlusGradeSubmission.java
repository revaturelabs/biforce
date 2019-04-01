package com.revature.Util;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class BatchPlusGradeSubmission {
	public static void calcGradeSubmission(JavaSparkContext context, SparkSession session, String input_batch, String input_assessment, String input_note, String output) {
		//Constructs a schema in the metastore for Spark to load data into and read from
		StructField batch_id = DataTypes.createStructField("batch_id", DataTypes.IntegerType, true);
        StructField borderline_grade_threshold = DataTypes.createStructField("borderline_grade_threshold", DataTypes.DoubleType, true);
		StructField end_date = DataTypes.createStructField("end_date", DataTypes.DateType, true);
        StructField good_grade_threshold = DataTypes.createStructField("good_grade_threshold", DataTypes.DoubleType, true);
		StructField location = DataTypes.createStructField("location", DataTypes.StringType, true);
        StructField skill_type = DataTypes.createStructField("skill_type", DataTypes.StringType, true);
		StructField start_date = DataTypes.createStructField("start_date", DataTypes.DateType, true);
        StructField training_name = DataTypes.createStructField("training_name", DataTypes.StringType, true);
		StructField training_type = DataTypes.createStructField("training_type", DataTypes.StringType, true);
        StructField number_of_weeks = DataTypes.createStructField("number_of_weeks", DataTypes.IntegerType, true);
        StructField co_trainer_id = DataTypes.createStructField("co_trainer_id", DataTypes.IntegerType, true);
        StructField trainer_id = DataTypes.createStructField("trainer_id", DataTypes.IntegerType, true);
        StructField resource_id = DataTypes.createStructField("resource_id", DataTypes.StringType, true);
        StructField address_id = DataTypes.createStructField("address_id", DataTypes.IntegerType, true);
        StructField graded_weeks = DataTypes.createStructField("graded_weeks", DataTypes.IntegerType, true);
        List<StructField> fields = new ArrayList<StructField>();
        fields.add(batch_id);
        fields.add(borderline_grade_threshold);
        fields.add(end_date);
        fields.add(good_grade_threshold);
        fields.add(location);
        fields.add(skill_type);
        fields.add(start_date);
        fields.add(training_name);
        fields.add(training_type);
        fields.add(number_of_weeks);
        fields.add(co_trainer_id);
        fields.add(trainer_id);
        fields.add(resource_id);
        fields.add(address_id);
        fields.add(graded_weeks);
        StructType schema = DataTypes.createStructType(fields);		
		Dataset<Row> data_batch = session.sqlContext().read().format("csv").option("delimiter", "~").option("header", "false").schema(schema).load(input_batch);
		data_batch.createOrReplaceTempView("caliber_batch");
		
		StructField assessment_id = DataTypes.createStructField("assessment_id", DataTypes.IntegerType, true);
        StructField raw_score = DataTypes.createStructField("raw_score", DataTypes.DoubleType, true);
		StructField assessment_title = DataTypes.createStructField("assessment_title", DataTypes.StringType, true);
        StructField assessment_type = DataTypes.createStructField("assessment_type", DataTypes.StringType, true);
		StructField week_number = DataTypes.createStructField("week_number", DataTypes.IntegerType, true);
        StructField batch_id2 = DataTypes.createStructField("batch_id", DataTypes.IntegerType, true);
		StructField assessment_category = DataTypes.createStructField("assessment_category", DataTypes.StringType, true);
        fields = new ArrayList<StructField>();
        fields.add(assessment_id);
        fields.add(raw_score);
        fields.add(assessment_title);
        fields.add(assessment_type);
        fields.add(week_number);
        fields.add(batch_id2);
        fields.add(assessment_category);
        schema = DataTypes.createStructType(fields);
		Dataset<Row> data_assessment = session.sqlContext().read().format("csv").option("delimiter", "~").option("header", "false").schema(schema).load(input_assessment);
		data_assessment.createOrReplaceTempView("caliber_assessment");
		
		StructField note_id = DataTypes.createStructField("note_id", DataTypes.IntegerType, true);
        StructField note_content = DataTypes.createStructField("note_content", DataTypes.DoubleType, true);
		StructField max_visibility = DataTypes.createStructField("max_visibility", DataTypes.StringType, true);
        StructField is_qc_feedback = DataTypes.createStructField("is_qc_feedback", DataTypes.StringType, true);
		StructField qc_status = DataTypes.createStructField("qc_status", DataTypes.IntegerType, true);
        StructField note_type = DataTypes.createStructField("note_type", DataTypes.IntegerType, true);
		StructField week_number = DataTypes.createStructField("week_number", DataTypes.StringType, true);
		StructField batch_id3 = DataTypes.createStructField("batch_id", DataTypes.StringType, true);
		StructField trainee_id = DataTypes.createStructField("trainee_id", DataTypes.StringType, true);
        fields = new ArrayList<StructField>();
        fields.add(note_id);
        fields.add(note_content);
        fields.add(max_visibility);
        fields.add(is_qc_feedback);
        fields.add(qc_status);
        fields.add(note_type);
        fields.add(week_number);
		fields.add(batch_id3);
		fields.add(trainee_id);
        schema = DataTypes.createStructType(fields);
		Dataset<Row> data_assessment = session.sqlContext().read().format("csv").option("delimiter", "~").option("header", "false").schema(schema).load(input_note);
		data_assessment.createOrReplaceTempView("caliber_note");
		
        //Executes SQL query to aggregate data
		Dataset<Row> BatchPlusGradeSubmissionTemp = session.sqlContext().sql("CREATE VIEW BatchPlusGradeSubmissionTemp  AS\r\n" + 
				"SELECT \r\n" + 
				"caliber_batch.batch_id, \r\n" + 
				"caliber_batch.borderline_grade_threshold, \r\n" + 
				"caliber_batch.end_date, \r\n" + 
				"caliber_batch.good_grade_threshold, \r\n" + 
				"caliber_batch.location, \r\n" + 
				"caliber_batch.skill_type, \r\n" + 
				"caliber_batch.start_date, \r\n" + 
				"caliber_batch.training_name, \r\n" + 
				"caliber_batch.training_type, \r\n" + 
				"caliber_batch.number_of_weeks, \r\n" + 
				"caliber_batch.co_trainer_id, \r\n" + 
				"caliber_batch.trainer_id, \r\n" + 
				"caliber_batch.resource_id, \r\n" + 
				"caliber_batch.address_id, \r\n" + 
				"caliber_batch.graded_weeks,\r\n" + 
				"test.graded_weeks_exam,\r\n" + 
				"test2.graded_weeks_verbal,\r\n" + 
				"test3.graded_weeks_target\r\n" + 
				"FROM caliber_batch\r\n" + 
				"LEFT JOIN\r\n" + 
				"    (SELECT count(DISTINCT week_number) AS graded_weeks_exam, batch_id \r\n" + 
				"    FROM caliber_assessment \r\n" + 
				"    WHERE assessment_type = 'Exam' \r\n" + 
				"    GROUP BY batch_id) AS test\r\n" + 
				"ON caliber_batch.batch_id = test.batch_id\r\n" + 
				"LEFT JOIN\r\n" + 
				"    (SELECT count(DISTINCT week_number) AS graded_weeks_verbal, batch_id\r\n" + 
				"    FROM caliber_assessment \r\n" + 
				"    WHERE assessment_type = 'Verbal' \r\n" + 
				"    GROUP BY batch_id) AS test2\r\n" + 
				"ON caliber_batch.batch_id = test2.batch_id\r\n" + 
				"LEFT JOIN\r\n" + 
				"    (SELECT max(week_number)-1 AS graded_weeks_target, batch_id\r\n" + 
				"    FROM caliber_note\r\n" + 
				"    WHERE is_qc_feedback = 1\r\n" + 
				"    GROUP BY batch_id) AS test3\r\n" + 
				"ON caliber_batch.batch_id = test3.batch_id;");
		
		Dataset<Row> BatchPlusGradeSubmission = session.sqlContext().sql("SELECT \r\n" + 
				"BatchPlusGradeSubmissionTemp.batch_id, \r\n" + 
				"BatchPlusGradeSubmissionTemp.borderline_grade_threshold, \r\n" + 
				"BatchPlusGradeSubmissionTemp.end_date, \r\n" + 
				"BatchPlusGradeSubmissionTemp.good_grade_threshold, \r\n" + 
				"BatchPlusGradeSubmissionTemp.location, \r\n" + 
				"BatchPlusGradeSubmissionTemp.skill_type, \r\n" + 
				"BatchPlusGradeSubmissionTemp.start_date, \r\n" + 
				"BatchPlusGradeSubmissionTemp.training_name, \r\n" + 
				"BatchPlusGradeSubmissionTemp.training_type, \r\n" + 
				"BatchPlusGradeSubmissionTemp.number_of_weeks, \r\n" + 
				"BatchPlusGradeSubmissionTemp.co_trainer_id, \r\n" + 
				"BatchPlusGradeSubmissionTemp.trainer_id, \r\n" + 
				"BatchPlusGradeSubmissionTemp.resource_id, \r\n" + 
				"BatchPlusGradeSubmissionTemp.address_id, \r\n" + 
				"BatchPlusGradeSubmissionTemp.graded_weeks,\r\n" + 
				"BatchPlusGradeSubmissionTemp.graded_weeks_exam,\r\n" + 
				"BatchPlusGradeSubmissionTemp.graded_weeks_verbal,\r\n" + 
				"BatchPlusGradeSubmissionTemp.graded_weeks_target,\r\n" + 
				"(BatchPlusGradeSubmissionTemp.graded_weeks_target - BatchPlusGradeSubmissionTemp.graded_weeks_exam) AS num_grades_missing_exam,\r\n" + 
				"(BatchPlusGradeSubmissionTemp.graded_weeks_target - BatchPlusGradeSubmissionTemp.graded_weeks_verbal) AS num_grades_missing_verbal\r\n" + 
				"FROM BatchPlusGradeSubmissionTemp;");

		//Write query results to S3
		BatchPlusGradeSubmission.write().format("csv").option("header", "true").save("s3a://revature-analytics-dev/dev1901/BatchPlusGradeSubmission.csv");
	}
}