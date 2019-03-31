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

public class SpecificWeeksSubmitted {
	public static void calcSpecificWeeks(JavaSparkContext context, SparkSession session, String input, String output) {
		//Constructs a schema in the metastore for Spark to load data into and read from
		StructField week_number_exam = DataTypes.createStructField("week_number_exam", DataTypes.DoubleType, true);
        StructField week_number_verbal = DataTypes.createStructField("week_number_verbal", DataTypes.DoubleType, true);
        StructField batch_id = DataTypes.createStructField("batch_id", DataTypes.DoubleType, true);
        StructField trainer_id = DataTypes.createStructField("trainer_id", DataTypes.StringType, true);
        
        List<StructField> fields = new ArrayList<StructField>();
        
        fields.add(week_number_exam);
        fields.add(week_number_verbal);
        fields.add(batch_id);
        fields.add(trainer_id);
        
        StructType schema = DataTypes.createStructType(fields);
		
		Dataset<Row> data = session.sqlContext().read().format("csv").option("delimiter", "~").option("header", "false").schema(schema).load(input);
		
		data.createOrReplaceTempView("SpecificWeeksSubmittedTable");
		
        //Executes SQL query to aggregate data
		Dataset<Row> SpecificWeeksSubmittedTable = session.sqlContext().sql(
		        "SELECT temp.week_number_exam, temp2.week_number_verbal, caliber_batch.batch_id, caliber_batch.trainer_id\r\n" + 
				"FROM caliber_batch\r\n" + 
				"JOIN\r\n" + 
				"    (SELECT caliber_assessment.week_number AS week_number_exam, caliber_assessment.batch_id AS batch_id\r\n" + 
				"    FROM caliber_assessment\r\n" + 
				"    JOIN caliber_grade ON caliber_assessment.assessment_id=caliber_grade.assessment_id\r\n" + 
				"    WHERE caliber_assessment.assessment_type = 'Exam'\r\n" + 
				"    GROUP BY caliber_assessment.batch_id, caliber_assessment.week_number) AS temp\r\n" + 
				"ON caliber_batch.batch_id = temp.batch_id\r\n" + 
				"FULL JOIN\r\n" + 
				"    (SELECT caliber_assessment.week_number AS week_number_verbal, caliber_assessment.batch_id AS batch_id\r\n" + 
				"    FROM caliber_assessment\r\n" + 
				"    JOIN caliber_grade ON caliber_assessment.assessment_id=caliber_grade.assessment_id\r\n" + 
				"    WHERE caliber_assessment.assessment_type = 'Verbal'\r\n" + 
				"    GROUP BY caliber_assessment.batch_id, caliber_assessment.week_number) AS temp2\r\n" + 
				"ON (caliber_batch.batch_id = temp2.batch_id and week_number_verbal = week_number_exam);");

		//Write query results to S3
		SpecificWeeksSubmittedTable.write().format("csv").option("header", "true").save("s3a://revature-analytics-dev/dev1901/SpecificWeeksSubmitted.csv");
	}
}