package com.revature;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.revature.spark.AnalyticResult;
import com.revature.spark.VerbalTestIndicator;

public class Driver {
	
	private static List<AnalyticResult> results = new ArrayList();
	
	public static void main(String args[]) {
		if (args.length != 3) {
			System.out.println("Wrong amount of inputs");
			System.exit(-1);
		}
		
		final String SPARK_MASTER = args[0];
		final String INPUT_PATH = args[1];
		final String OUTPUT_PATH = args[2];
		
		/*
		 * Set Spark configuration for Context
		 */
		
		SparkConf conf = new SparkConf()
				.setAppName("ChanceToFail").setMaster(SPARK_MASTER);
		JavaSparkContext context = new JavaSparkContext(conf);
		SparkSession session = new SparkSession(context.sc());
		
		/*
		 * Run the WordCount Spark Logic
		 */
		
		Dataset<Row> csv = session.read().format("csv").option("header","false").load(INPUT_PATH);
		Row row = csv.first();
		
		results.add(new VerbalTestIndicator().execute(csv,row,1));
		results.add(new VerbalTestIndicator().execute(csv,row,2));
		results.add(new VerbalTestIndicator().execute(csv,row,3));
		
		int totalSampleSize = 0;
		for (AnalyticResult result:results) {
			totalSampleSize+=result.getSampleSize();
		}
		
		double finalPercentage = -1;
		for (AnalyticResult result:results) {
			System.out.println(result.toString());
			finalPercentage += result.getPercentage() * (result.getSampleSize()/(double)totalSampleSize);
		}
		
		System.out.println(finalPercentage);
		
		session.close();
		context.close();
	}
}
