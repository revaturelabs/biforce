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

public class RedFlags {
	public static void raiseFlag(JavaSparkContext context, SparkSession session, String topicProfiencyInput, String batchInput, String assessmentInput, String noteInput, String output, String output2) {
     
		//Performs evaluation from Topic Proficiency
        
		StructField qcScore = DataTypes.createStructField("QC_Score", DataTypes.DoubleType, true);
        
        StructField weightedScore = DataTypes.createStructField("Weighted_Score", DataTypes.DoubleType, true);
        
        StructField week = DataTypes.createStructField("Week", DataTypes.DoubleType, true);
        
        StructField subject = DataTypes.createStructField("Subject", DataTypes.StringType, true);
        
        StructField assignmentType = DataTypes.createStructField("Assignment_Type", DataTypes.StringType, true);
        
        StructField Trainee_ID = DataTypes.createStructField("Trainee_ID", DataTypes.DoubleType, true);
        
        StructField TraineeName = DataTypes.createStructField("Trainee_Name", DataTypes.StringType, true);
        
        StructField Trainer_ID = DataTypes.createStructField("Trainer_ID", DataTypes.DoubleType, true);
        
        StructField TrainerName = DataTypes.createStructField("Trainer_Name", DataTypes.StringType, true);
        
        StructField Batch_Name = DataTypes.createStructField("Batch_Name", DataTypes.StringType, true);
        
        List<StructField> fields = new ArrayList<StructField>();
        
        fields.add(qcScore);
        fields.add(weightedScore);
        fields.add(week);
        fields.add(subject);
        fields.add(assignmentType);
        fields.add(Trainee_ID);
        fields.add(TraineeName);
        fields.add(Trainer_ID);
        fields.add(TrainerName);
        fields.add(Batch_Name);
        
        StructType schema = DataTypes.createStructType(fields);
		
		Dataset<Row> data = session.sqlContext().read().format("csv").option("delimiter", "~").option("header", "false").schema(schema).load(topicProfiencyInput);
		
		data.createOrReplaceTempView("TopicProficiency");
		//TODO: move this shit up with other data prep part
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
        fields = new ArrayList<StructField>();
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
        schema = DataTypes.createStructType(fields);		
        Dataset<Row> data_batch = session.sqlContext().read().format("csv").schema(schema).option("delimiter", "~").option("header", "false").option("inferSchema", "false").option("mode", "PERMISSIVE").option("nullValue", "null").load(batchInput);
        
		data_batch.createOrReplaceTempView("caliber_batch");
		
		//data_batch.show();
		
		StructField assessment_id = DataTypes.createStructField("assessment_id", DataTypes.DoubleType, true);
        StructField raw_score = DataTypes.createStructField("raw_score", DataTypes.DoubleType, true);
		StructField assessment_title = DataTypes.createStructField("assessment_title", DataTypes.StringType, true);
        StructField assessment_type = DataTypes.createStructField("assessment_type", DataTypes.StringType, true);
		StructField week_number = DataTypes.createStructField("week_number", DataTypes.DoubleType, true);
        StructField batch_id2 = DataTypes.createStructField("batch_id", DataTypes.DoubleType, true);
		StructField assessment_category = DataTypes.createStructField("assessment_category", DataTypes.DoubleType, true);
        fields = new ArrayList<StructField>();
        fields.add(assessment_id);
        fields.add(raw_score);
        fields.add(assessment_title);
        fields.add(assessment_type);
        fields.add(week_number);
        fields.add(batch_id2);
        fields.add(assessment_category);
        schema = DataTypes.createStructType(fields);
		Dataset<Row> data_assessment = session.sqlContext().read().format("csv").option("delimiter", "~").option("header", "false").option("inferSchema", "false").option("mode", "PERMISSIVE").option("nullValue", "null").schema(schema).load(assessmentInput);
		data_assessment.createOrReplaceTempView("caliber_assessment");
		
		StructField note_id = DataTypes.createStructField("note_id", DataTypes.DoubleType, true);
        StructField note_content = DataTypes.createStructField("note_content", DataTypes.StringType, true);
		StructField max_visibility = DataTypes.createStructField("max_visibility", DataTypes.DoubleType, true);
        StructField is_qc_feedback = DataTypes.createStructField("is_qc_feedback", DataTypes.DoubleType, true);
		StructField qc_status = DataTypes.createStructField("qc_status", DataTypes.StringType, true);
        StructField note_type = DataTypes.createStructField("note_type", DataTypes.StringType, true);
		StructField week_number2 = DataTypes.createStructField("week_number", DataTypes.DoubleType, true);
		StructField batch_id3 = DataTypes.createStructField("batch_id", DataTypes.DoubleType, true);
		StructField trainee_id = DataTypes.createStructField("trainee_id", DataTypes.DoubleType, true);
        fields = new ArrayList<StructField>();
        fields.add(note_id);
        fields.add(note_content);
        fields.add(max_visibility);
        fields.add(is_qc_feedback);
        fields.add(qc_status);
        fields.add(note_type);
        fields.add(week_number2);
		fields.add(batch_id3);
		fields.add(trainee_id);
        schema = DataTypes.createStructType(fields);
		Dataset<Row> data_note = session.sqlContext().read().format("csv").option("delimiter", "~").option("header", "false").option("inferSchema", "false").option("mode", "PERMISSIVE").option("nullValue", "null").schema(schema).load(noteInput);
		data_note.createOrReplaceTempView("caliber_note");
        		
		Dataset<Row> proficiency = session.sqlContext().sql("select Trainer_ID, Trainer_Name, Subject, round(avg(Weighted_Score), 1) AverageScore from TopicProficiency where Weighted_Score is not null group by Trainer_Name, Trainer_ID, Subject");

		//Save as temp table
		proficiency.createOrReplaceTempView("WeightedScores");
		
		//Executes SQL query to aggregate data in real-time
		
		Dataset<Row> averages = session.sqlContext().sql("SELECT AVG(averagescore) AS Average, STDDEV(averagescore) AS Std_Dev FROM WeightedScores");
		
		averages.createOrReplaceTempView("Averages");
		
		Dataset<Row> proficiencyRedFlags = session.sqlContext().sql("SELECT Trainer_ID, Trainer_Name, Subject, Averagescore FROM WeightedScores FULL OUTER JOIN Averages WHERE Averagescore < (Average - Std_Dev)");

		proficiencyRedFlags.coalesce(1).write().format("csv").option("header", "true").mode("Overwrite").save(output);
		
        //Executes SQL query to aggregate data
		
		//Dataset<Row> BatchPlusGradeSubmissionTemp = data_batch.join(data_assessment.select());
		
		//todo: translate all this into Spark goodness
		
		//Dataset<Row> test = data_batch.select(data_batch.col("batch_id")
		
		Dataset<Row> BatchPlusGradeSubmissionTemp = session.sqlContext().sql(
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
				"LEFT JOIN\r\n" + //make dataset out of this
				"    (SELECT count(DISTINCT week_number) AS graded_weeks_exam, batch_id \r\n" + 
				"    FROM caliber_assessment \r\n" + 
				"    WHERE assessment_type = 'Exam' \r\n" + 
				"    GROUP BY batch_id) AS test\r\n" + 
				"ON caliber_batch.batch_id = test.batch_id\r\n" + 
				"LEFT JOIN\r\n" + //make dataset out of this
				"    (SELECT count(DISTINCT week_number) AS graded_weeks_verbal, batch_id\r\n" + 
				"    FROM caliber_assessment \r\n" + 
				"    WHERE assessment_type = 'Verbal' \r\n" + 
				"    GROUP BY batch_id) AS test2\r\n" + 
				"ON caliber_batch.batch_id = test2.batch_id\r\n" + 
				"LEFT JOIN\r\n" + //make dataset out of this
				"    (SELECT max(week_number)-1 AS graded_weeks_target, batch_id\r\n" + 
				"    FROM caliber_note\r\n" + 
				"    WHERE is_qc_feedback = 1\r\n" + 
				"    GROUP BY batch_id) AS test3\r\n" + 
				"ON caliber_batch.batch_id = test3.batch_id");
	
		
		Dataset<Row> bpgs = BatchPlusGradeSubmissionTemp; 
		
		Dataset<Row> BatchPlusGradeSubmission = BatchPlusGradeSubmissionTemp.select(bpgs.col("batch_id"),
																					bpgs.col("borderline_grade_threshold"),
																					bpgs.col("end_date"),
																					bpgs.col("good_grade_threshold"),
																					bpgs.col("location"),
																					bpgs.col("skill_type"),
																					bpgs.col("start_date"),
																					bpgs.col("training_name"),
																					bpgs.col("training_type"),
																					bpgs.col("number_of_weeks"),
																					bpgs.col("co_trainer_id"),
																					bpgs.col("trainer_id"),
																					bpgs.col("resource_id"),
																					bpgs.col("address_id"),
																					bpgs.col("graded_weeks"),
																					bpgs.col("graded_weeks_exam"),
																					bpgs.col("graded_weeks_verbal"),
																					bpgs.col("graded_weeks_target"),
																					bpgs.col("graded_weeks_target").minus(bpgs.col("graded_weeks_exam")).name("num_grades_missing_exam"),
																					bpgs.col("graded_weeks_target").minus(bpgs.col("graded_weeks_verbal")).name("num_grades_missing_verbal")
		);
		
		Dataset<Row> missingMoreThanTwo = BatchPlusGradeSubmission.filter(BatchPlusGradeSubmission.col("num_grades_missing_exam").$greater$eq(2).or(BatchPlusGradeSubmission.col("num_grades_missing_verbal").$greater$eq(2)));
		
		missingMoreThanTwo.coalesce(1).write().format("csv").option("header", "true").mode("Overwrite").save(output2);
		
	}
}
