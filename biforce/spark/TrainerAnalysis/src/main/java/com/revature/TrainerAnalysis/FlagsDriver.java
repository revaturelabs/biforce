package com.revature.TrainerAnalysis;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import com.revature.Util.*;


		
//NOTE: You will need Spark 2.4.0 in order to run this. Remember that Spark is NOT backwards compatible!

public class FlagsDriver {
	public static void main(String[] args) {
		final String inputPath = args[0];
		final String inputPath2 = args[1];
		final String outputPath = "s3a://revature-analytics-dev/TrainerAnalysis/RedFlags";
		
		

		SparkConf conf = new SparkConf().setAppName("TrainerAnalysis_RedFlags");
		
		JavaSparkContext context = new JavaSparkContext(conf);
		
		SparkSession session = new SparkSession(context.sc());

		//Call for method from outside packages for processing

		RedFlags.raiseFlag(context, session, inputPath, inputPath2, outputPath);
		
		//Close open resources
		
		session.close();
		
		context.close();
		
	}
}