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

public class NormalizeScores {
	public static void normalization(JavaSparkContext context, SparkSession session, String input, String output) {
		
		//Constructs a schema in the metastore for Spark to load data into and read from
        
        StructField score = DataTypes.createStructField("Score", DataTypes.DoubleType, true);
        
        StructField week = DataTypes.createStructField("Week", DataTypes.DoubleType, true);
        
        StructField subject = DataTypes.createStructField("Subject", DataTypes.StringType, true);
        
        StructField BatchId = DataTypes.createStructField("Batch_Id", DataTypes.DoubleType, true);
        
        StructField TraineeId = DataTypes.createStructField("Trainee_Id", DataTypes.DoubleType, true);
        
        StructField TraineeName = DataTypes.createStructField("Trainee_Name", DataTypes.StringType, true);
        
        StructField TrainerName = DataTypes.createStructField("Trainer_Name", DataTypes.StringType, true);
        
        StructField Batch_Name = DataTypes.createStructField("Batch_Name", DataTypes.StringType, true);
        
        List<StructField> fields = new ArrayList<StructField>();
        
        fields.add(score);
        fields.add(week);
        fields.add(subject);
        fields.add(BatchId);
        fields.add(TraineeId);
        fields.add(TraineeName);
        fields.add(TrainerName);
        fields.add(Batch_Name);
        
        StructType schema = DataTypes.createStructType(fields);
		
		Dataset<Row> data = session.sqlContext().read().format("csv").option("delimiter", "~").option("header", "false").schema(schema).load(input);
		
		data.createOrReplaceTempView("TrainerScores");
        
		
		//Executes SQL query to aggregate data in real-time
		
		Dataset<Row> normalizedScores = session.sqlContext().sql("SELECT Trainer_Name, Subject, Avg_Score, Std_Score, (Avg_Score - mini) / (maxi - mini) AS Normalized_Score FROM (SELECT Trainer_Name, Subject, avg(Score) AS Avg_Score, stddev(Score) AS Std_Score, max(Score) AS maxi, min(Score) AS mini FROM TrainerScores GROUP BY Trainer_Name, Subject) ORDER BY Trainer_Name");

		//Write query results to S3
		normalizedScores.coalesce(1).write().format("csv").option("header", "true").option("delimiter", "~").mode("Overwrite").save(output);
	}
}
