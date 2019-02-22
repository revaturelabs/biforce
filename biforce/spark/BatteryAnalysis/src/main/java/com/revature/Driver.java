package com.revature;

import static org.apache.spark.sql.functions.col;

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

/**
 * <h1> Biforce Spark Team JavaDoc </h1>
 * <p>This class consists exclusively of a main method that interacts with utilities
 * which interact with a logistic regression model. It has several helper methods
 * which write output. All methods are static.</p>
 * <p>The model returns percentage values which indicate likelihood to fail and then
 * runs through all possible cutoff values to determine the optimal cutoff percentage. </p>
 * <p>The model clumps trainees together in groups for each of the first few weeks by
 * their test scores into buckets based on equi-distant percentiles. The model then
 * calculates the odds of someone in that bucket passing or failing and then calculates
 * the log odds taken from this tutorial. <a href="http://vassarstats.net/logreg1.html"><>.</p>
 * <p>TODO: describe the rest of log-reg logic.</p>
 * <p>This class is a member of the
 * <a href="https://github.com/Dec-17-Big-Data/biforce">
 * Revature Biforce</a> Github project.</p>
 * <p>For more information regarding this project see the
 * <a href="https://drive.google.com/open?id=1xD-x-0oX2vXWdEpoMhq0Jpu0j2gCgpjOufVh5NerJHQ">
 * Biforce Living Document</a>.</p>
 * @author  Mason Wegert
 * @author  Diego Gomez
 * @author  Tim Law
 * @author  Pil Ju Chun
 **/
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

	private static BufferedWriter writer, controlWriter;
	private static JavaSparkContext context;
	private static SparkSession session;

	/**
	 * This method creates the spark context and session and reads the input value. 
	 * Then it calls a plethora of utility functions. Primarily t performs ETL, splitting, 
	 * training the model, testing the model, and printing the results.
	 * @param args - 0 input file location, 1 is main output, 2 is model parameters output
	 */
	public static void main(String args[]) {
		// Configure spark, get session variable, declare Datasets		
		context = new JavaSparkContext(new SparkConf().setAppName("ChanceToFail"));
		context.setLogLevel("ERROR");
		session = new SparkSession(context.sc());
		Dataset<Row> csv,filtered_csv,controlData,modelData;

		// read input csv, header is optional to name each column, 
		// inferSchema optimizes storage/operations by storing variables
		// as a datatype other than string
		csv = session.read().format("csv").option("header","false").option("inferSchema", "true").load(args[0]);

		double accuracyDelta = 0.01; // For cutoff point precision
		double[] splitRatios = {0.7,0.3}; // Split of control & model data

		int modelSplitCount = 10; // # of buckets. 10 seems to be good.

		initWriters(args[1], args[2]);

		// Filter the indicator data to include only the valid data for our samples.
		System.out.println("Filtering out irrelevant data...");
		// Note that associate status (c10) is consistent across all test weeks as it's from a relational DB
		filtered_csv = csv.filter("_c10 = 0 OR _c10 = 1");

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

		JavaRDD<Row> controlRDD_wk1 = ModelApplier.applyControlModel(controlData, modelParams, 1);
		JavaRDD<Row> controlRDD_wk2 = ModelApplier.applyControlModel(controlData, modelParams, 2);
		JavaRDD<Row> controlRDD = ModelApplier.applyControlModel(controlData, modelParams, 3);

		try {
			controlWriter.append("Stats for week 1 data ONLY\n");
			applyControl(controlRDD_wk1, accuracyDelta);
			controlWriter.append("Stats for week 1 & 2 data ONLY\n");
			applyControl(controlRDD_wk2, accuracyDelta);
			controlWriter.append("Stats using data for weeks 1-3\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		OptimalPoint optimalPoint = applyControl(controlRDD, accuracyDelta);
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
			controlWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		session.close();
		context.close();
	}

	/**
	 * Initializes the BufferedWriter/FileWriter class combination for two writers.
	 * One main writer with the id/prediction using mainPath, one for control data
	 * stats using controlPath.
	 * @param mainPath - file system location for main Writer
	 * @param controlPath - file system location for wk3Writer
	 */
	private static void initWriters(String writerPath, String controlPath) {
		try {
			writer = new BufferedWriter(new FileWriter(writerPath, false));
			writer.append("battery_id,% Chance to Fail,Prediction\n");
			controlWriter = new BufferedWriter(new FileWriter(controlPath, false));
			controlWriter.append("Control data statistics\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * It prints the formula for calculating the probability of failure based on the modelParams.
	 * Prints to the console and wk3Writer for each of the 3 exam types (one for each week).
	 * @param modelParams
	 */
	private static void printModel(double[][] modelParams) {
		for(int i = 0; i < 3; i++) {
			String s = String.format("Exam type " + (i+1) +": partialFailChance = e^(%2.3f*score+%2.3f) / (1+e^(%2.3f*score+%2.3f), r^2 = %1.3f\n", 
					modelParams[i][1],modelParams[i][2],modelParams[i][1],modelParams[i][2],modelParams[i][3]);
			System.out.println(s);
			try {
				controlWriter.append(s);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param controlRDD
	 * @param dropPercent
	 */
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

				controlWriter.append(outString);
			} catch (IOException e) {
				System.out.println("IOException");
				e.printStackTrace();
			}
		});
	}

	private static OptimalPoint applyControl(JavaRDD<Row> controlRDD, double accuracyDelta) {
		controlRDD.cache();
		// Finds the drop % cutoff point where the number of incorrect guesses is minimized
		OptimalPoint optimalPoint = ModelApplier.findOptimalPercent(controlRDD, accuracyDelta);
		
		writeToControl("Fail percent: " + Math.round(optimalPoint.getOptimalPercent()*10000)/10000.0 + "\nCorrect estimates: " + 
				optimalPoint.getOptimalAccurateCount() + "\nTotal Count: " + controlRDD.count() + "\nAccuracy: " + 
				(double)optimalPoint.getOptimalAccurateCount()/(double)controlRDD.count() + "\n\n", controlWriter);

		controlRDD.unpersist();
		return optimalPoint;
	}
	
	private static void writeToControl(String outString, BufferedWriter controlWriter) {
		try {
			System.out.println(outString);
			controlWriter.append(outString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}