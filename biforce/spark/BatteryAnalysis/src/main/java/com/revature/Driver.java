package com.revature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.revature.util.EvaluationMetrics;
import com.revature.util.ModelApplier;
import com.revature.util.ModelFunction;
import com.revature.util.OptimalPoint;
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

	private static BufferedWriter writer;
	private static BufferedWriter accuracyWriter;
	private static JavaSparkContext context;
	private static SparkSession session;

	public static void main(String args[]) {
		context = new JavaSparkContext(new SparkConf().setAppName("ChanceToFail"));
		context.setLogLevel("ERROR");
		session = new SparkSession(context.sc());
		Dataset<Row> csv,filtered_csv,controlData,modelData;
		
		csv = session.read().format("csv").option("header","false").option("inferSchema", "true").load(args[0]);

		double controlPrecision = 1.0;
		double[] splitRatios = {0.7,0.3};

		initWriters(args[1],args[2]);

		// Filter the indicator data to include only the valid data for our samples.
		filtered_csv = csv.filter("_c10 = 0 OR _c10 = 1 OR (_c10 = 2 AND (_c4 = 9 OR _c4 = 10))");

		Dataset<Row>[] splits = filtered_csv.select("_c9").distinct().randomSplit(splitRatios,42); // use seed (second arg of randomSplit) for testing
		modelData = filtered_csv.join(splits[0], filtered_csv.col("_c9").equalTo(splits[0].col("_c9")), "leftsemi");
		controlData = filtered_csv.join(splits[1], filtered_csv.col("_c9").equalTo(splits[1].col("_c9")), "leftsemi");

		modelData.persist(); // holds modeldata in memory so it doesn't have to repeat the above filters/joins (transformations)
		double[][] modelParams = ModelFunction.execute(modelData, PartitionFinder.read(modelData));
		modelData.unpersist();

		printModel(modelParams);

		JavaRDD<Row> controlRDD = ModelApplier.applyControlModel(controlData, modelParams);
		controlRDD.cache();

		OptimalPoint optimalPoint = ModelApplier.findOptimalPercent(controlRDD, controlPrecision);

		writeToControl("Fail percent: " + Math.round(optimalPoint.getOptimalPercent()*10000)/10000.0 + "\nCorrect estimates: " + 
				optimalPoint.getOptimalAccurateCount() + "\nTotal Count: " + controlRDD.count() + "\nAccuracy: " + 
				(double)optimalPoint.getOptimalAccurateCount()/(double)controlRDD.count() + "\n");

		writeControlOutput(controlRDD, optimalPoint.getOptimalPercent());

		// TODO
		// calculate/print evaluation metrics
		// Assumed controlRDD is testing test data with model already applied to third column
		System.out.println("Mean Absolute Error: " + EvaluationMetrics.testMAE(controlRDD));
		System.out.println("Root Mean Squared Error: " + EvaluationMetrics.testRMSE(controlRDD));

		JavaPairRDD<Integer, Row> appliedResultPair = ModelApplier.applyModel(csv, modelParams, optimalPoint.getOptimalPercent());

		appliedResultPair.foreach(pairTuple -> {
			String prediction = pairTuple._2.getDouble(1)/pairTuple._2.getDouble(2) >= optimalPoint.getOptimalPercent() ? "DROP" : "PASS";
			writer.append(pairTuple._1 + "," + pairTuple._2.getDouble(1)/pairTuple._2.getDouble(2) + "," + prediction + "\n");
		});

		// Close all the resources.
		try {
			controlRDD.unpersist();
			csv.unpersist();
			writer.close();
			accuracyWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		session.close();
		context.close();
	}


	private static void initWriters(String mainPath, String controlPath) {
		try {
			writer = new BufferedWriter(new FileWriter(mainPath, false));
			accuracyWriter = new BufferedWriter(new FileWriter(controlPath, false));
			accuracyWriter.append("Control data statistics\n");
			writer.append("battery_id,% Chance to Fail,Prediction\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printModel(double[][] modelParams) {
		for(int i = 0; i < 3; i++) {
			String s = String.format("Exam " + (i+1) +": partialFailChance = e^(%2.3f*score+%2.3f) / (1+e^(%2.3f*score+%2.3f), r^2 = %1.3f\n", 
					modelParams[i][1],modelParams[i][2],modelParams[i][1],modelParams[i][2],modelParams[i][3]);
			System.out.println(s);
			try {
				accuracyWriter.append(s);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void writeControlOutput(JavaRDD<Row> controlRDD, double dropPercent) {
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
	}

	private static void writeToControl(String outString) {
		try {
			System.out.println(outString);
			accuracyWriter.append(outString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}