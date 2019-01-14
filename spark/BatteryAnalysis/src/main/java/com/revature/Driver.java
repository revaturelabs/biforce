package com.revature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.revature.spark.AnalyticResult;
import com.revature.spark.PassFailSampleFilter;
import com.revature.spark.TestIndicator;

public class Driver {
	
	private static List<AnalyticResult> results = new ArrayList();
	private static BufferedWriter writer;
	private static Dataset<Row> csv,filtered_csv;
	private static int count = 0;
	
	/**
	 * Creates the output file, filters the data to just the relevant values,
	 * then runs the tests on each unique id.
	 */
	
	public static void main(String args[]) {
		
		final String SPARK_MASTER = args[0];
		final String INPUT_PATH = args[1];
		final String OUTPUT_PATH = args[2];
		
		try {
			writer = new BufferedWriter(new FileWriter(OUTPUT_PATH, true));
			writer.append("battery_id,% Chance to Fail, Total Sample Size\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Set Spark configuration for Context
		 */
		
		SparkConf conf = new SparkConf().setAppName("ChanceToFail").setMaster(SPARK_MASTER);
		JavaSparkContext context = new JavaSparkContext(conf);
		context.setLogLevel("ERROR");
		SparkSession session = new SparkSession(context.sc());
		
		/*
		 * Read in the data from the input file
		 * _c0 = test type
		 * _c1 = raw score
		 * _c2 = score
		 * _c3 = test period
		 * _c4 = test category
		 * _c5 = builder id
		 * _c6 = group id
		 * _c7 = group type
		 * _c8 = battery id
		 * _c9 = battery status
		 */
		
		//Read the input file in as a spark Dataset<Row> with no header, therefor the
		//resulting table column names are in the format _c#
		csv = session.read().format("csv").option("header","false").load(INPUT_PATH);
		
		//Create a list containing each row with battery id as a primary key
		
		Dataset<Row> uniqueBatteries = csv.groupBy("_c8").count();
		
		List<Row> rowList = uniqueBatteries.toJavaRDD().collect();
		
		//Filter the indicator data to include only the valid data for our samples.
		filtered_csv = PassFailSampleFilter.execute(csv);
		
		//Run the tests for each battery id
		for (Row row : rowList) {
			performTestingOnRows(csv.filter("_c8 = " + row.get(0).toString()));
		}
		
		//close all the resources
		
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		session.close();
		context.close();
	}
	
	/**
	 * Call the various indicator tests and add the results to our result list.
	 * At the moment these consist of looking at just the first 3 tests in the first 3
	 * periods.
	 */
	
	public static void performTestingOnRows(Dataset<Row> battery_id_tests) {
		int totalSampleSize;
		double finalPercentage;
		AnalyticResult newResult;
		int input_battery_id = Integer.parseInt(battery_id_tests.first().getString(8).toString());
			
		System.out.println(count + ". Beginning Analysis on battery id: " + input_battery_id);
		
			for (int i = 1;i<4;i++)
				for (int j = 1;j<4; j++) {
					newResult = new TestIndicator().execute(filtered_csv,battery_id_tests,i,j);
					results.add(newResult);
					if (newResult!=null)
						System.out.println(newResult);
				}
			
			//Sum up the total of the sample sizes for each result
			
			totalSampleSize = 0;
			for (AnalyticResult result:results) {
				if (result!=null) {
					totalSampleSize+=result.getSampleSize();
				}
			}
			
			//Use the sample size sum and calculate the final percentage by weighing each result by their sample size
			finalPercentage = 0;
			for (AnalyticResult result:results) {
				if (result!=null) {
					finalPercentage += result.getPercentage() * (result.getSampleSize()/(double)totalSampleSize);
				}
			}
			
			System.out.println("Aggregated Result: Battery_id: " + input_battery_id + ", % Chance to fail: " + finalPercentage + ", Total Sample Size: " + totalSampleSize);
			//Append the results to the output file.
			
			try {
				writer.append(input_battery_id + "," + finalPercentage + "," + totalSampleSize);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			count++;
			
			//Clear the results of these tests to make way for the next battery id
			results.clear();
	}
}
