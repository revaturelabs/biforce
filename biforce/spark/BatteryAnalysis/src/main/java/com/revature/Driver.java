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
import static org.apache.spark.sql.functions.col;

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

		// Minweek/maxweek are inclusive. Default values should be [1,3]
		int minWeek = 1;
		int maxWeek = 1;
		double controlPrecision = 1.0; // For cutoff point precision
		double[] splitRatios = {0.7,0.3}; // Split of control & model data
		int modelSplitCount = 10; // # of buckets. 10 seems to be good.

		initWriters(args[1], args[2]);

		// Filter the indicator data to include only the valid data for our samples.
		System.out.println("Filtering out irrelevant data...");
		// Note that associate status (c10) is consistent across all test weeks as it's from a relational DB
		filtered_csv = csv.filter("_c10 = 0 OR _c10 = 1"); 
		Dataset<Row> currentWeek = csv.groupBy("_c9").max("_c4").where("max(_c4) >= " + minWeek).withColumnRenamed("_c9", "id");
		filtered_csv = filtered_csv.join(currentWeek, col("_c9").equalTo(col("id")), "leftsemi");
		
		filtered_csv.filter("_c4 <= " + maxWeek);
		
		// Random split of associates
		Dataset<Row>[] splits = filtered_csv.select("_c9").distinct().randomSplit(splitRatios,41); // use seed (second arg of randomSplit) for testing
		modelData = filtered_csv.join(splits[0], filtered_csv.col("_c9").equalTo(splits[0].col("_c9")), "leftsemi");
		controlData = filtered_csv.join(splits[1], filtered_csv.col("_c9").equalTo(splits[1].col("_c9")), "leftsemi");
		
		// Build model from modelData
		modelData.persist(); // holds modeldata in memory so it doesn't have to repeat the above filters/joins (transformations)
		double[][] modelParams = ModelFunction.execute(modelData, PartitionFinder.read(modelData, modelSplitCount), modelSplitCount);
		modelData.unpersist();

		// Writes the logarithmic model to the file specified in args[2]
		printModel(modelParams);

		JavaRDD<Row> controlRDD = ModelApplier.applyControlModel(controlData, modelParams);
		
		controlRDD.cache();
		// Finds the drop % cutoff point where the number of incorrect guesses is minimized
		OptimalPoint optimalPoint = ModelApplier.findOptimalPercent(controlRDD, controlPrecision);

		writeToControl("Fail percent: " + Math.round(optimalPoint.getOptimalPercent()*10000)/10000.0 + "\nCorrect estimates: " + 
				optimalPoint.getOptimalAccurateCount() + "\nTotal Count: " + controlRDD.count() + "\nAccuracy: " + 
				(double)optimalPoint.getOptimalAccurateCount()/(double)controlRDD.count() + "\n\n");

		writeControlOutput(controlRDD, optimalPoint.getOptimalPercent());

		// TODO
		// calculate/print evaluation metrics
		// Assumed controlRDD is testing test data with model already applied to third column
		System.out.println("Mean Absolute Error: " + EvaluationMetrics.testMAE(controlRDD));
		System.out.println("Root Mean Squared Error: " + EvaluationMetrics.testRMSE(controlRDD));

		JavaPairRDD<Integer, Row> appliedResultPair = ModelApplier.applyModel(csv, modelParams);

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
			String s = String.format("Exam type " + (i+1) +": partialFailChance = e^(%2.3f*score+%2.3f) / (1+e^(%2.3f*score+%2.3f), r^2 = %1.3f\n", 
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