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

public class TopicProficiency {
	public static void calculate_Proficiency(JavaSparkContext context, SparkSession session, String input, String output) {
		
		//Constructs a schema in the metastore for Spark to load data into and read from
        
        StructField qcScore = DataTypes.createStructField("QC_Score", DataTypes.DoubleType, true);
        
        StructField weightedScore = DataTypes.createStructField("Weighted_Score", DataTypes.DoubleType, true);
        
        StructField week = DataTypes.createStructField("Week", DataTypes.DoubleType, true);
        
        StructField subject = DataTypes.createStructField("Subject", DataTypes.StringType, true);
        
        StructField assignmentType = DataTypes.createStructField("Assignment_Type", DataTypes.StringType, true);
        
        StructField TraineeName = DataTypes.createStructField("Trainee_Name", DataTypes.StringType, true);
        
        StructField TrainerName = DataTypes.createStructField("Trainer_Name", DataTypes.StringType, true);
        
        StructField Batch_Name = DataTypes.createStructField("Batch_Name", DataTypes.StringType, true);
        
        List<StructField> fields = new ArrayList<StructField>();
        
        fields.add(qcScore);
        fields.add(weightedScore);
        fields.add(week);
        fields.add(subject);
        fields.add(assignmentType);
        fields.add(TraineeName);
        fields.add(TrainerName);
        fields.add(Batch_Name);
        
        StructType schema = DataTypes.createStructType(fields);
		
		Dataset<Row> data = session.sqlContext().read().format("csv").option("delimiter", "~").option("header", "false").schema(schema).load(input);
		
		data.createOrReplaceTempView("TopicProficiency");
        
		//Executes SQL query to aggregate data in real-time
		
		Dataset<Row> proficiency = session.sqlContext().sql("select Trainer_Name, Subject, round(avg(Weighted_Score), 1) AverageScore from TopicProficiency where Weighted_Score is not null group by Trainer_Name, Subject");

		//Write query results to S3
		proficiency.write().format("csv").option("header", "true").save("s3a://revature-analytics-dev/dev1901/TopicProficiency.csv");
	}
}
