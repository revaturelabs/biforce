package com.revature.TrainerAnalysis;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import com.revature.Util.*;


		
//NOTE: You will need Spark 2.4.0 in order to run this. Remember that Spark is NOT backwards compatible!

public class GradeSubmissionDriver {
	public static void main(String[] args) {
		final String inputPath = args[0];
		final String inputPath2 = args[1];
		final String inputPath3 = args[2];
		final String outputPath = args[3];
		
		

		SparkConf conf = new SparkConf().setAppName("TrainerAnalysis_GradeSubmission");
		
		JavaSparkContext context = new JavaSparkContext(conf);
		
		SparkSession session = new SparkSession(context.sc());

		//Call for method from outside packages for processing

		BatchPlusGradeSubmission.calcGradeSubmission(context, session, inputPath, inputPath2, inputPath3, outputPath);

		//Close open resources
		
		session.close();
		
		context.close();
		
	}
}