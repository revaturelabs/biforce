package com.revature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;;

public class Driver {

	private static BufferedWriter writer;
	private static Dataset<Row> csv,filtered_csv,controlData,modelData;
	private static int count = 0;
	private static JavaSparkContext context;
	
	/*
	 * Creates the output file, filters the data to just the relevant values,
	 * then runs the tests on each unique id.
	 */
	public static void main(String args[]) {
		
		double[] splitRatios = {0.8,0.2};

		try {
			writer = new BufferedWriter(new FileWriter(args[1], true));
			writer.append("battery_id,% Chance to Fail\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set Spark configuration for Context.
		SparkConf conf = new SparkConf().setAppName("ChanceToFail");
		context = new JavaSparkContext(conf);
		context.setLogLevel("ERROR");
		SparkSession session = new SparkSession(context.sc());
		
		/*
		 * Read in the data from the input file
		 * _c0 = PK
		 * _c1 = test type
		 * _c3 = score
		 * _c4 = test period
		 * _c9 = associate id
		 * _c10 = associate status
		 */

		// Read the input file in as a spark Dataset<Row> with no header, therefore the
		// resulting table column names are in the format _c#.
		csv = session.read().format("csv").option("header","false").load(args[0]);

		// Filter the indicator data to include only the valid data for our samples.
		filtered_csv = csv.filter("_c10 = 0 OR _c10 = 1 OR (_c10 = 2 AND (_c4 = 9 OR _c4 = 10))");
		// Dataset<Row> testCounts = filtered_csv.groupBy("_c9").count();
		// filtered_csv.javaRDD().filter(row->row.getInt(x) > 2); //do something like this for filter >2 tests
		
		Dataset<Row>[] splits = filtered_csv.randomSplit(splitRatios);
		modelData = splits[0];
		controlData = splits[1];

		// Build a logarithmic model with modeldata each test
		/* xyz */
		// model built

		//apply model to each row
		performTestingOnRows(csv);
		
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
	public static void performTestingOnRows(Dataset<Row> data) {
		JavaRDD<Row> associateIDs = data.drop("_c0", "_c1","_c2","_c3","_c4","_c5","_c6","_c7","_c8","_c10").dropDuplicates().javaRDD();
		// We have a class that takes a test # 0-6 and outputs a % chance of being dropped
		// We also have to retrieve r^2's from this class, weight the drop %, and combine
		associateIDs.map(new FindTestScore());
	}
	
	
	private static class FindTestScore implements Function<Row,Double>{
		private static final long serialVersionUID = -3035931770787238206L;

		public Double call(Row r){
			double finalPercentage = 0;
			long associateID = r.getLong(0);
			System.out.println(count + ". Beginning Analysis on battery id: " + associateID);
			context.sc().log().info(count + ". Beginning Analysis on battery id: " + associateID);
			
			List<Double> dropChances = new ArrayList<>();
			List<Double> correlations = new ArrayList<>();
			List<Row> associateTests = csv.where("_c9=" + associateID).collectAsList();
			
			double total_r = 0;
			for (Row row:associateTests) {
				//test type: row.getInt(1)
				//week number: row.getInt(4)
				//test score: row.getDouble(3)
				/*
				 * Use class method to grab %
				 */
				row.get(1);
				dropChances.add(5.0);
				correlations.add(0.7);
				total_r += 0.7;
			}
			
			for (int i = 0;i<dropChances.size();++i) {
				finalPercentage += dropChances.get(i)*correlations.get(i)/total_r;
			}
			
			try {
				writer.append(associateID + "," + finalPercentage + '\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
			context.sc().log().info("Aggregated Result: Battery_id: " + associateID + ", % Chance to fail: " + finalPercentage);
			System.out.println("Aggregated Result: Battery_id: " + associateID + ", % Chance to fail: " + finalPercentage);
			return finalPercentage;
		}
	}
}
