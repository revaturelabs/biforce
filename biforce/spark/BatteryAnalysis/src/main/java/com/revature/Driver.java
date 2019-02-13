package com.revature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/*
 * _c0 = PK
 * _c1 = test type
 * _c3 = score
 * _c4 = test period
 * _c9 = associate id
 * _c10 = associate status
 */

// Read the input file in as a spark Dataset<Row> with no header, therefore the
// resulting table column names are in the format _c#.

public class Driver {

	private static BufferedWriter writer;
	private static JavaSparkContext context;
	public static Dataset<Row> csv,filtered_csv,controlData,modelData;
	private static SparkSession session;
	private static SparkConf conf;

	public static void main(String args[]) {
		double[] splitRatios = {0.8,0.2};
		try {
			writer = new BufferedWriter(new FileWriter(args[1], true));
			writer.append("battery_id,% Chance to Fail\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		conf = new SparkConf().setAppName("ChanceToFail");
		context = new JavaSparkContext(conf);
		context.setLogLevel("ERROR");
		session = new SparkSession(context.sc());
		csv = session.read().format("csv").option("header","false").load(args[0]);
		
		csv.cache();
		
		// Filter the indicator data to include only the valid data for our samples.
		filtered_csv = csv.filter("_c10 = 0 OR _c10 = 1 OR (_c10 = 2 AND (_c4 = 9 OR _c4 = 10))");

		//		Dataset<Row> testCounts = filtered_csv.groupBy("_c9").count();
		//		filtered_csv.javaRDD().filter(row->row.getInt(x) > 2); //do something like this for filter >2 tests

		Dataset<Row>[] splits = filtered_csv.randomSplit(splitRatios);
		modelData = splits[0];
		controlData = splits[1];

		// Build a logarithmic model with modeldata each test
		/* xyz */
		// model built
		//apply model to each row
		performTestingOnRows();

		// use controlData to test accuracy

		// Close all the resources.
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		session.close();
		context.close();
	}

	// This method writes output to file based on 'csv'
	public static void performTestingOnRows() {
		// get rid of .orderBy if running slowly
		List<Row> IDs = csv.drop("_c0", "_c1","_c2","_c3","_c4","_c5","_c6","_c7","_c8","_c10").dropDuplicates().orderBy("_c9").collectAsList();

		for (Row r:IDs) {
			Double finalPercentage = 0.0;
			long associateID = Long.parseLong(r.getString(0));

			System.out.println("Beginning Analysis on battery id: " + associateID);
			context.sc().log().info("Beginning Analysis on battery id: " + associateID);

			List<Row> associateTests = new ArrayList<Row>();

			associateTests = csv.where("_c9=" + associateID).collectAsList();

			double total_r = 0;
			for (Row row:associateTests) {
				//test type: row.getInt(1)
				//week number: row.getInt(4)
				//test score: row.getDouble(3)
				row.get(1);

				finalPercentage += Double.parseDouble(row.getString(3))*0.7;
				total_r += 0.7;
			}
			finalPercentage /= total_r;

			try {
				writer.append(associateID + "," + finalPercentage + '\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
			context.sc().log().info("> Aggregated Result: Battery_id: " + associateID + ", % Chance to fail: " + finalPercentage);
			System.out.println("> Aggregated Result: Battery_id: " + associateID + ", % Chance to fail: " + finalPercentage);
		}
	}
}