package com.revature.TrainerAnalysis;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import com.revature.Util.*;


		
//NOTE: You will need Spark 2.4.0 in order to run this. Remember that Spark is NOT backwards compatible!

public class WeeksSubmittedDriver {
	public static void main(String[] args) {
		final String inputPath = args[0]; //batch input
		final String inputPath2 = args[1]; //assessment input
		final String inputPath3 = args[2]; //grade input
		
		//uncomment this if you wish to write to S3
		//final String outputPath = "s3a://revature-analytics-dev/TrainerAnalysis/SpecificWeeksSubmitted";
				
		//comment this line out if you dont want to write to cloudera local
		final String outputPath = "/home/cloudera/SpecificWeeksSubmitted";
		
		
		
		SparkConf conf = new SparkConf().setAppName("TrainerAnalysis_WeeksSubmitted");
		
		JavaSparkContext context = new JavaSparkContext(conf);
		
		SparkSession session = new SparkSession(context.sc());

		//Call for method from outside packages for processing

		SpecificWeeksSubmittedTable.calcSpecificWeeks(context, session, inputPath, inputPath2, inputPath3, outputPath);

		//Close open resources
		
		session.close();
		
		context.close();
		
	}
}