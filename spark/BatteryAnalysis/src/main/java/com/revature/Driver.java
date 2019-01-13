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
import com.revature.spark.TestIndicator;

public class Driver {
	
	private static List<AnalyticResult> results = new ArrayList();
	private static BufferedWriter writer;
	private static Dataset<Row> csv;
	private static int max = 0;
	
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
		
		
		csv = session.read().format("csv").option("header","false").load(INPUT_PATH);
		csv = csv.filter("(_c9 = 1 OR _c9 = 2) AND (_c3 = 1 OR _c3 = 2 OR _c3 = 3)");
		
		Dataset<Row> uniqueBatteries = csv.groupBy("_c8").count();
		
		List<Row> rowList = uniqueBatteries.toJavaRDD().collect();
		
		for (Row row : rowList) {
			performTestingOnRow(Integer.parseInt(row.get(0).toString()));
		}
		
//		System.out.println(csv.first());
//		uniqueBatteries.foreach((ForeachFunction<Row>) row ->  {
//				System.out.println(csv.toString());
//				csv.show();
//				//performTestingOnRow(Integer.parseInt(row.get(0).toString()));
//			}
//		);
		//Write the final results to a file in csv format. This takes the form of a table
		//with 2 columns: the battery id and the % chance they will fail.
		
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		session.close();
		context.close();
	}
	
	public static void performTestingOnRow(int input_battery_id) {
		int totalSampleSize;
		double finalPercentage;
		if (max<5) {
			/*
			 * Call the various indicator tests and add the results to our result list.
			 */
			
			for (int i = 0;i<4;i++)
				for (int j = 0;j<3; j++) {
					results.add(new TestIndicator().execute(csv.select("*"),input_battery_id,i,j));
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
			
			try {
				writer.append(input_battery_id + "," + finalPercentage + "," + totalSampleSize+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			max++;
		}
	}
}
