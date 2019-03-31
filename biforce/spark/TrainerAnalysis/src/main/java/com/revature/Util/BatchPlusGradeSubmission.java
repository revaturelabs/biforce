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
	public static void calcGradeSubmission(JavaSparkContext context, SparkSession session, String input, String output) {
		//Constructs a schema in the metastore for Spark to load data into and read from
		StructField batch_id = DataTypes.createStructField("Batch_ID", DataTypes.DoubleType, true);
        StructField borderline_grade_threshold = DataTypes.createStructField("borderline_grade_threshold", DataTypes.DoubleType, true);
        StructField end_date = DataTypes.createStructField("end_date", DataTypes.DoubleType, true);
        StructField good_grade_threshold = DataTypes.createStructField("good_grade_threshold", DataTypes.StringType, true);
        StructField location = DataTypes.createStructField("location", DataTypes.StringType, true);
        StructField skill_type = DataTypes.createStructField("skill_type", DataTypes.DoubleType, true);
        StructField start_date = DataTypes.createStructField("start_date", DataTypes.DoubleType, true);
        StructField training_name = DataTypes.createStructField("training_name", DataTypes.StringType, true);
        StructField training_type = DataTypes.createStructField("training_type", DataTypes.StringType, true);
        StructField number_of_weeks = DataTypes.createStructField("number_of_weeks", DataTypes.StringType, true);
		StructField co_trainer_id = DataTypes.createStructField("co_trainer_id", DataTypes.StringType, true);
		StructField trainer_id = DataTypes.createStructField("trainer_id", DataTypes.StringType, true);
		StructField resource_id = DataTypes.createStructField("resource_id", DataTypes.StringType, true);
		StructField address_id = DataTypes.createStructField("address_id", DataTypes.StringType, true);
		StructField graded_weeks = DataTypes.createStructField("graded_weeks", DataTypes.StringType, true);
		StructField graded_weeks_exam = DataTypes.createStructField("graded_weeks_exam", DataTypes.StringType, true);
		StructField graded_weeks_verbal = DataTypes.createStructField("graded_weeks_verbal", DataTypes.StringType, true);
		StructField graded_weeks_target = DataTypes.createStructField("graded_weeks_target", DataTypes.StringType, true);
        
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
		fields.add(graded_weeks_exam);
		fields.add(graded_weeks_verbal);
		fields.add(graded_weeks_target);
        
        StructType schema = DataTypes.createStructType(fields);
		
		Dataset<Row> data = session.sqlContext().read().format("csv").option("delimiter", "~").option("header", "false").schema(schema).load(input);
		
		data.createOrReplaceTempView("BatchPlusGradeSubmission");
		
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