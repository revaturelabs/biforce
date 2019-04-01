package com.revature.TrainerAnalysis;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import com.revature.Util.*;


		
//NOTE: You will need Spark 2.4.0 in order to run this. Remember that Spark is NOT backwards compatible!

public class NormalizationDriver {
	public static void main(String[] args) {
		final String inputPath = args[0];
		final String outputPath = args[1];
		
		

		SparkConf conf = new SparkConf().setAppName("TrainerAnalysis_NormalizeScores");
		
		JavaSparkContext context = new JavaSparkContext(conf);
		
		SparkSession session = new SparkSession(context.sc());

		//Call for method from outside packages for processing
		
		NormalizeScores.normalization(context, session, inputPath, outputPath);
		
		//Close open resources
		
		session.close();
		
		context.close();
		
	}
}
