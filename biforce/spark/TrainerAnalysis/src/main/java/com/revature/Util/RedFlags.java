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
	public static void raiseFlag(JavaSparkContext context, SparkSession session, String input, String output) {
        //If my logic is correct, we could access the prior TempViews from the other methods and use those to query for results.
		
		//Executes SQL query to aggregate data in real-time
		
		Dataset<Row> redFlags = session.sqlContext().sql("add query here");

		//Write query results to S3
		redFlags.write().format("csv").option("header", "true").save("s3a://revature-analytics-dev/dev1901/RedFlags.csv");
	}
}
