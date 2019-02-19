package com.revature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.revature.util.EvaluationMetrics;
import com.revature.util.ModelApplier;
import com.revature.util.ModelFunction;
import com.revature.util.PartitionFinder;

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

		ModelApplier modelApplier = new ModelApplier(args[1], args[2]);

		// Filter the indicator data to include only the valid data for our samples.
		filtered_csv = csv.filter("_c10 = 0 OR _c10 = 1 OR (_c10 = 2 AND (_c4 = 9 OR _c4 = 10))");

		Dataset<Row>[] splits = filtered_csv.select("_c9").distinct().randomSplit(splitRatios,42); // use seed (second arg) for testing
		modelData = filtered_csv.join(splits[0], filtered_csv.col("_c9").equalTo(splits[0].col("_c9")), "leftsemi");
		controlData = filtered_csv.join(splits[1], filtered_csv.col("_c9").equalTo(splits[1].col("_c9")), "leftsemi");

		modelData.persist(); // holds modeldata in memory so it doesn't have to repeat the above filters/joins

		List<List<Double>> partitions = PartitionFinder.read(modelData);

		double[][] bin1 = ModelFunction.execute(modelData, partitions);
		
		modelData.unpersist();

		for(int i = 0; i < 3; i++) {
			System.out.println(bin1[i][0] + " " + bin1[i][1]+ " " + bin1[i][2]+ " " + bin1[i][3]);
		}

		JavaRDD<Row> controlRDD = modelApplier.applyControlModel(controlData, bin1);
		
		final double dropPercent = modelApplier.findOptimalPercent(controlRDD, bin1, controlPrecision);
		
		modelApplier.writeControlOutput(controlRDD, dropPercent);
		
		// TODO
		// calculate/print evaluation metrics
		// Assumed controlRDD is testing test data with model already applied to third column
		System.out.println("Mean Absolute Error: " + EvaluationMetrics.testMAE(controlRDD));
		System.out.println("Root Mean Squared Error: " + EvaluationMetrics.testRMSE(controlRDD));
		
		modelApplier.applyModel(csv, bin1, dropPercent);
		
		// Close all the resources.
		csv.unpersist();
		modelApplier.close();
		session.close();
		context.close();
	}
}