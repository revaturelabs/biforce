package com.revature;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import com.revature.spark.WordCount;

public class Driver {
	private static final String APPLICATION_NAME = "Word Count";
	
	public static void main(String[] args) {
		if (args.length != 4) {
			System.out.printf(
					"Usage: WordCount <spark master> <input dir> <output dir> <min count>\n");
			System.exit(-1);
		}

		final String SPARK_MASTER = args[0];
		final String INPUT_PATH = args[1];
		final String OUTPUT_PATH = args[2];
		final Integer MIN_COUNT = Integer.parseInt(args[3]);
		
		/*
		 * Set up the configuration and instantiate the context.
		 * Most of the cases we get a local[2] as the Spark Master
		 * 
		 * local[2] is considered Spark development mode,
		 * great for learning purposes
		 */
		SparkConf conf = new SparkConf().setAppName(APPLICATION_NAME).setMaster(SPARK_MASTER);
		JavaSparkContext context = new JavaSparkContext(conf);
		
		/*
		 * Call your spark method, it can be named anything
		 */
		new WordCount().execute(context, INPUT_PATH, OUTPUT_PATH, MIN_COUNT);
	}
}
