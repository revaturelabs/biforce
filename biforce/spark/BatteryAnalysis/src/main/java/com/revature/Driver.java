package com.revature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

import com.revature.util.PartitionFinder;

import scala.Tuple2;

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
		conf = new SparkConf().setAppName("ChanceToFail");
		context = new JavaSparkContext(conf);
		context.setLogLevel("ERROR");
		session = new SparkSession(context.sc());
		csv = session.read().format("csv").option("header","false").option("inferSchema", "true").load(args[0]);

		double[] splitRatios = {0.8,0.2};
		try {
			writer = new BufferedWriter(new FileWriter(args[1], true));
			writer.append("battery_id,% Chance to Fail\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		csv.cache();

		// Filter the indicator data to include only the valid data for our samples.
		filtered_csv = csv.filter("_c10 = 0 OR _c10 = 1 OR (_c10 = 2 AND (_c4 = 9 OR _c4 = 10))");

		//		Dataset<Row> testCounts = filtered_csv.groupBy("_c9").count();
		//		filtered_csv.javaRDD().filter(row->row.getInt(x) > 2); //do something like this for filter >2 tests

		Dataset<Row>[] splits = filtered_csv.randomSplit(splitRatios);
		modelData = splits[0];
		controlData = splits[1];


		//List<List<Double>> partitions = PartitionFinder.read(filtered_csv);

		// Build a logarithmic model with modeldata each test
		/* xyz */
		// model built
		//apply model to each row

		//performTestingOnRows();

		List<Double> correlationList = new ArrayList<>();
		correlationList.add(0.7);
		correlationList.add(0.86);
		correlationList.add(0.44);
		correlationList.add(0.6);
		correlationList.add(0.7 + 0.86 + 0.44 + 0.6);

		JavaRDD<Row> csvRDD = csv
				.javaRDD()
				.map(row->{
					// row should have 13 cols now. Col 0-10 as before, col 11 as "result", col 12 as "weight"
					double failPercent = 0;
					double rValue = 0;
					for (int i=1;i<=4;++i) {
						if (row.getInt(1) == i) { // Assessment type 1-4
							failPercent = row.getDouble(3) * correlationList.get(i - 1);
							rValue = correlationList.get(i-1);
							break;
						}
					}
					// row: (int, int, int, double, int, int, int, int, int, int, int)
					Row outputRow = RowFactory.create(row.getInt(9), failPercent, rValue);
					return outputRow;
				});

		JavaPairRDD<Integer,Row> applicationRDD = csvRDD.mapToPair(row -> new Tuple2<Integer,Row>(row.getInt(0),row));
		JavaPairRDD<Integer, Row> sums = applicationRDD.reduceByKey((Row row1,Row row2)->{
			return RowFactory.create(row1.getInt(0), row1.getDouble(1) + row2.getDouble(1), row1.getDouble(2) + row2.getDouble(2));});
		
		// Join tuple(id,sum(r*fail%)) and tuple(id,sum(r)) to get tuple(id,tuple(r*fail%, sum(r)))
		sums.foreach(t22 -> {
			String s = t22._1 + "," + t22._2.getDouble(1)/t22._2.getDouble(2) + "\n";
			writer.append(s);
			System.out.println(s);
		});

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
		// orderBy("_c9") if testing
		List<Row> IDs = csv.drop("_c0", "_c1","_c2","_c3","_c4","_c5","_c6","_c7","_c8","_c10").dropDuplicates().collectAsList();

		for (Row r:IDs) {
			Double finalPercentage = 0.0;
			int associateID = r.getInt(0);

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

				finalPercentage += row.getDouble(3)*0.7;
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
			return;
		}
	}
}