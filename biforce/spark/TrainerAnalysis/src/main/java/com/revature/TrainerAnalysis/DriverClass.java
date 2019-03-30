package com.revature.TrainerAnalysis;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import com.revature.Util.*;

//how to get this to read from hive

//1. Copy core-site.xml, hdfs-site.xml, and hive-site.xml to /etc/spark/conf
//2. Edit spark-env.sh to include 'export HADOOP_CONF_DIR=/etc/hadoop/conf/' (or whereever hadoop config is)

//Currently, its set up to work in the cloudera VM environment, so things will need to be reconfigured for when we deploy things!
		
//NOTE: You will need Spark 2.4.0 in order to run this. Remember that Spark is NOT backwards compatible!

public class DriverClass {
	public static void main(String[] args) {
		final String inputPath = args[0];
		final String outputPath = args[1];
		
		
		//Be sure to have the data in hive before you try and run this
		//See readme under ETL for how to do that
		
		SparkConf conf = new SparkConf().setAppName("TrainerAnalysis");
		
		JavaSparkContext context = new JavaSparkContext(conf);
		
		SparkSession session = new SparkSession(context.sc());

		//Call for methods from outside packages for processing
		
		NormalizeScores.normalization(context, session, inputPath, outputPath);
		
		TopicProficiency.calculate_Proficiency(context, session, inputPath, outputPath);
		
		PercentileRange.calculateRange(context, session, inputPath, outputPath);
		
		GradeSubmission.countGrades(context, session, inputPath, outputPath);
		
		RedFlags.raiseFlag(context, session, inputPath, outputPath);
		
		//Close open resources
		
		session.close();
		
		context.close();
		
	}
}
