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

public class SpecificWeeksSubmittedTable {
	public static void calcSpecificWeeks(JavaSparkContext context, SparkSession session, String input_batch, String input_assessment, String input_grade, String output) {
		
		//Constructs a schema in the metastore for Spark to load data into and read from
		StructField batch_id = DataTypes.createStructField("batch_id", DataTypes.DoubleType, true);
        StructField borderline_grade_threshold = DataTypes.createStructField("borderline_grade_threshold", DataTypes.DoubleType, true);
		StructField end_date = DataTypes.createStructField("end_date", DataTypes.StringType, true);
        StructField good_grade_threshold = DataTypes.createStructField("good_grade_threshold", DataTypes.DoubleType, true);
		StructField location = DataTypes.createStructField("location", DataTypes.StringType, true);
        StructField skill_type = DataTypes.createStructField("skill_type", DataTypes.StringType, true);
		StructField start_date = DataTypes.createStructField("start_date", DataTypes.StringType, true);
        StructField training_name = DataTypes.createStructField("training_name", DataTypes.StringType, true);
		StructField training_type = DataTypes.createStructField("training_type", DataTypes.StringType, true);
        StructField number_of_weeks = DataTypes.createStructField("number_of_weeks", DataTypes.DoubleType, true);
        StructField co_trainer_id = DataTypes.createStructField("co_trainer_id", DataTypes.DoubleType, true);
        StructField trainer_id = DataTypes.createStructField("trainer_id", DataTypes.DoubleType, true);
        StructField resource_id = DataTypes.createStructField("resource_id", DataTypes.StringType, true);
        StructField address_id = DataTypes.createStructField("address_id", DataTypes.DoubleType, true);
        StructField graded_weeks = DataTypes.createStructField("graded_weeks", DataTypes.DoubleType, true);
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
		
		StructField assessment_id = DataTypes.createStructField("assessment_id", DataTypes.DoubleType, true);
        StructField raw_score = DataTypes.createStructField("raw_score", DataTypes.DoubleType, true);
		StructField assessment_title = DataTypes.createStructField("assessment_title", DataTypes.StringType, true);
        StructField assessment_type = DataTypes.createStructField("assessment_type", DataTypes.StringType, true);
		StructField week_number = DataTypes.createStructField("week_number", DataTypes.DoubleType, true);
        StructField batch_id2 = DataTypes.createStructField("batch_id", DataTypes.DoubleType, true);
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
		
		StructField grade_id = DataTypes.createStructField("assessment_id", DataTypes.DoubleType, true);
        StructField date_received = DataTypes.createStructField("raw_score", DataTypes.StringType, true);
		StructField score = DataTypes.createStructField("assessment_title", DataTypes.DoubleType, true);
        StructField assessment_id2 = DataTypes.createStructField("assessment_type", DataTypes.StringType, true);
		StructField trainee_id = DataTypes.createStructField("week_number", DataTypes.DoubleType, true);
        fields = new ArrayList<StructField>();
        fields.add(grade_id);
        fields.add(date_received);
        fields.add(score);
        fields.add(assessment_id2);
        fields.add(trainee_id);
        schema = DataTypes.createStructType(fields);		
		Dataset<Row> data_grade = session.sqlContext().read().format("csv").option("delimiter", "~").option("header", "false").schema(schema).load(input_grade);
		data_grade.createOrReplaceTempView("caliber_grade");
		
        //Executes SQL query to aggregate data
		
		Dataset<Row> SpecificWeeksSubmittedTable = session.sqlContext().sql(
		        "SELECT temp.week_number_exam, temp2.week_number_verbal, caliber_batch.batch_id, caliber_batch.trainer_id FROM caliber_batch JOIN (SELECT caliber_assessment.week_number AS week_number_exam, caliber_assessment.batch_id AS batch_id FROM caliber_assessment JOIN caliber_grade ON caliber_assessment.assessment_id=caliber_grade.assessment_id WHERE caliber_assessment.assessment_type = 'Exam' GROUP BY caliber_assessment.batch_id, caliber_assessment.week_number) AS temp ON caliber_batch.batch_id = temp.batch_id FULL JOIN (SELECT caliber_assessment.week_number AS week_number_verbal, caliber_assessment.batch_id AS batch_id FROM caliber_assessment JOIN caliber_grade ON caliber_assessment.assessment_id=caliber_grade.assessment_id WHERE caliber_assessment.assessment_type = 'Verbal' GROUP BY caliber_assessment.batch_id, caliber_assessment.week_number) AS temp2 ON (caliber_batch.batch_id = temp2.batch_id and week_number_verbal = week_number_exam)");
		
		//Write query results to S3
		
		SpecificWeeksSubmittedTable.coalesce(1).write().format("csv").option("header", "true").mode("Overwrite").save(output);
	}
}