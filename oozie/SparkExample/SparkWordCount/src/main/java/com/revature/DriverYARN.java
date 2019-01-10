package com.revature;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import com.revature.spark.WordCount;

/*
 * The difference in this driver is that we are not setting the SparkMaster through the code
 * but with the actual command. We have less command line arguments now.
 * 
 * NOTE: You won't be able to run this in Cloudera with spark-submit since Cloudera is using Spark 1.6.0
 * and our code is for Spark 2.4.0 (Which AWS EMR has)
 * 
 * Command for Spark Cluster
 * spark-submit --master yarn --class com.revature.DriverYARN WordCountSpark.jar <hdfs-input> <hdfs-output> <minCount>
 */
public class DriverYARN {
	private static final String APPLICATION_NAME = "Word Count";
	
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.printf(
					"Usage: WordCount <input dir> <output dir> <min count>\n");
			System.exit(-1);
		}
		
		final String INPUT_PATH = args[0];
		final String OUTPUT_PATH = args[1];
		final Integer MIN_COUNT = Integer.parseInt(args[2]);
		
		/*
		 * Set up the configuration and instantiate the context.
		 * Most of the cases we get a local[2] as the Spark Master
		 * 
		 * local[2] is considered Spark development mode,
		 * great for learning purposes
		 */
		SparkConf conf = new SparkConf().setAppName(APPLICATION_NAME);
		JavaSparkContext context = new JavaSparkContext(conf);
		
		/*
		 * Call your spark method, it can be named anything
		 */
		new WordCount().execute(context, INPUT_PATH, OUTPUT_PATH, MIN_COUNT);
	}
}
