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

import com.revature.util.ModelFunction;
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
	private static BufferedWriter accuracyWriter;
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

		double controlPrecision = 1.0;
		double[] splitRatios = {0.7,0.3};

		try {
			writer = new BufferedWriter(new FileWriter(args[1], false));
			accuracyWriter = new BufferedWriter(new FileWriter(args[2], false));
			accuracyWriter.append("Control data statistics\n");
			writer.append("battery_id,% Chance to Fail\n");
		} catch (IOException e) {
			e.printStackTrace();
		}


		// Filter the indicator data to include only the valid data for our samples.
		filtered_csv = csv.filter("_c10 = 0 OR _c10 = 1 OR (_c10 = 2 AND (_c4 = 9 OR _c4 = 10))");

		Dataset<Row>[] splits = filtered_csv.select("_c9").distinct().randomSplit(splitRatios,42); // use seed (second arg) for testing
		modelData = filtered_csv.join(splits[0], filtered_csv.col("_c9").equalTo(splits[0].col("_c9")), "leftsemi");
		controlData = filtered_csv.join(splits[1], filtered_csv.col("_c9").equalTo(splits[1].col("_c9")), "leftsemi");

//		modelData.persist(); // holds modeldata in memory so it doesn't have to repeat the above filters/joins
//
//		List<List<Double>> partitions = PartitionFinder.read(modelData);
//
//		double[][] bin1 = ModelFunction.execute(modelData, partitions);
//		
//		modelData.unpersist();
		double[][] bin1 = new double[4][4];
		bin1[0][0] = 1.0;
		bin1[1][0] = 2.0;
		bin1[2][0] = 3.0;
		bin1[3][0] = 4.0;
		bin1[0][1] = -0.07378938896351178;
		bin1[1][1] = -0.11094627913463939;
		bin1[2][1] = -0.05932924706050152;
		bin1[3][1] = -0.017487144707690996;
		bin1[0][2] = 5.629783101381262;
		bin1[1][2] = 8.20653998389569;
		bin1[2][2] = 4.574615396517751;
		bin1[3][2] = 0.8891341343062747;
		bin1[0][3] = 0.5953324069167315;
		bin1[1][3] = 0.7209645285370248;
		bin1[2][3] = 0.7017632087421138;
		bin1[3][3] = 0.18873783916790474;


		for(int i = 0; i < 4; i++) {
			System.out.println(bin1[i][0] + " " + bin1[i][1]+ " " + bin1[i][2]+ " " + bin1[i][3]);
		}

		applyModel(csv, bin1);

		double optimalPercent = 0;
		long optimalAccurateCount = 0;
		List<Double> percentList = new ArrayList<>();

		for (Double d = 0.0;d <= 100; d+= controlPrecision) {
			percentList.add(d);
		}

		JavaRDD<Row> controlRDD = applyControlModel(controlData, bin1);
		controlRDD.cache();

		for (int i = 0;i<percentList.size();++i) {
			// controlRDD: Associate ID | % failure | fail (0 or 1 where 0 is fail)
			double d = percentList.get(i);

			// Filter out those who passed, and those who failed but we guessed wrong
			long accurateFailedCount = controlRDD.filter(row -> row.getInt(2) != 0 || row.getDouble(1) < d).count();
			// Filter out those who failed, and those who passed but we guessed wrong
			long accuratePassedCount = controlRDD.filter(row -> row.getInt(2) == 0 || row.getDouble(1) >= d).count();
			System.out.println("d: " + d + ", fail: " + accurateFailedCount + ", pass: " + accuratePassedCount);
			long accurateCount = accurateFailedCount + accuratePassedCount;

			if (accurateCount > optimalAccurateCount) {
				optimalAccurateCount = accurateCount;
				optimalPercent = percentList.get(i);
			}
		}
		
		System.out.println(optimalPercent);
		System.out.println(optimalAccurateCount);

		try {
			accuracyWriter.append("Fail percent: " + optimalPercent + "\nCorrect estimates: " + optimalAccurateCount +
					"\nTotal Count: " + controlRDD.count() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		final double dropPercent = optimalPercent;
		controlRDD.foreach(row -> {
			try {
				String outString;
				// if individual chance of failure is higher than threshhold -> DROP
				if (row.getDouble(1) >= dropPercent) {
					outString = row.getInt(0) + "," + row.getDouble(1) + "," + row.getInt(2) + "," + "DROP\n";
				} else {
					outString = row.getInt(0) + "," + row.getDouble(1) + "," + row.getInt(2) + "," + "PASS\n";
				}
				
				accuracyWriter.append(outString);
			} catch (IOException e) {
				System.out.println("IOException");
				e.printStackTrace();
			}
		});
		// use controlData to test accuracy

		// Close all the resources.
		try {
			csv.unpersist();
			writer.close();
			accuracyWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		session.close();
		context.close();
	}

	// This method is nearly instant. No optimization needed.
	private static void applyModel(Dataset<Row> csv, double[][] bin1) {
		JavaRDD<Row> csvRDD = 
				csv
				.javaRDD()
				.map(row->{
					// row should have 13 cols now. Col 0-10 as before, col 11 as "result", col 12 as "weight"
					double failPercent = 0;
					double rValue = 0;
					for (int i=1;i<=4;++i) {
						if (row.getInt(1) == i) { // Assessment type 1-3
							failPercent = Math.exp(row.getDouble(3) * bin1[i-1][1] + bin1[i-1][2])/
									(1 + Math.exp(row.getDouble(3) * bin1[i-1][1] + bin1[i-1][2])) *
									bin1[i-1][3];
							rValue = bin1[i-1][3];
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

		sums.foreach(pairTuple -> {
			String s = pairTuple._1 + "," + pairTuple._2.getDouble(1)/pairTuple._2.getDouble(2) + "\n";
			writer.append(s);
		});
	}

	private static JavaRDD<Row> applyControlModel(Dataset<Row> csv, double[][] bin1) {
		JavaRDD<Row> csvRDD = 
				csv
				.javaRDD()
				.map(row->{
					// row should have 13 cols now. Col 0-10 as before, col 11 as "result", col 12 as "weight"
					double failPercent = 0;
					double rValue = 0;

					for (int i=1;i<=4;++i) {
						if (row.getInt(1) == i) { // Assessment type 1-3
							failPercent = Math.exp(row.getDouble(3) * bin1[i-1][1] + bin1[i-1][2])/
									(1 + Math.exp(row.getDouble(3) * bin1[i-1][1] + bin1[i-1][2])) *
									bin1[i-1][3];
							rValue = bin1[i-1][3];
							break;
						}
					}

					int status = row.getInt(10) == 0 ? 0 : 1;

					// row: (int, int, int, double, int, int, int, int, int, int, int)
					Row outputRow = RowFactory.create(row.getInt(9), failPercent, rValue, status);
					return outputRow;
				});

		JavaPairRDD<Integer,Row> applicationRDD = csvRDD.mapToPair(row -> new Tuple2<Integer,Row>(row.getInt(0),row));
		JavaPairRDD<Integer, Row> sums = applicationRDD.reduceByKey((Row row1,Row row2)->{
			return RowFactory.create(row1.getInt(0), row1.getDouble(1) + row2.getDouble(1), row1.getDouble(2) + row2.getDouble(2), row1.getInt(3));});

		return sums.map(pairTuple -> {
			// Associate ID | % failure | fail (1 or 0)
			return RowFactory.create(pairTuple._1, pairTuple._2.getDouble(1)/pairTuple._2.getDouble(2), pairTuple._2.getInt(3));
		});
	}
}