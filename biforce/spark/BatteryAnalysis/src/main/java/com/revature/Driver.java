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
 * the log odds taken from this tutorial. <a href="http://vassarstats.net/logreg1.html">Logistic Regression</a>.</p>
 * <p>TODO: describe the rest of log-reg logic.</p>
 * <p>This class is a member of the
 * <a href="https://github.com/Dec-17-Big-Data/biforce">
 * Revature Biforce</a> Github project.</p>
 * <p>For more information regarding this project see the
 * <a href="https://drive.google.com/open?id=1xD-x-0oX2vXWdEpoMhq0Jpu0j2gCgpjOufVh5NerJHQ">
 * Biforce Living Document</a>.</p>
 * <h2>Used columns</h2>
 * <ul>
 * <li>_c0 = PK</li>
 * <li>_c1 = test type</li>
 * <li>_c3 = score</li>
 * <li>_c4 = test period</li>
 * <li>_c9 = associate id</li>
 * <li>_c10 = associate status</li>
 * </ul>
 * <h2>All Columns</h2>
 * <ul>
 * <li>_c0 = 1 : row number</li>
 * <li>_c1 = 2 : A.ASSESSMENT_TYPE
 *  - 'VERBAL' = 1; 'EXAM' = 2;
 *  - 'PROJECT' = 3; 'OTHER' = 4</li>
 * <li>_c2 = 25 : A.RAW_SCORE</li>
 * <li>_c3 = 74.00 : Q.SCORE</li>
 * <li>_c4 = 7 : A.WEEK_NUMBER 1-9 inclusive</li>
 * <li>_c5 = 20 : A.ASSESSMENT_CATEGORY</li>
 * <li>_c6 = 14 : G.TRAINER_ID</li>
 * <li>_c7 = 112511 : G.BATCH_ID</li>
 * <li>_c8 = 2 : G.SKILL_TYPE 
 *  - 'SDET' = 1; 'J2EE' = 2; 'OTHER' = 3;
 *  - 'BPM' = 4; 'NET' = 5; 'MICROSERVICES' = 6</li>
 * <li>_c9 = 281214 : Q.TRAINEE_ID</li>
 * <li>_c10 = 2 : B.TRAINING_STATUS 
 *  - 'DROPPED'= 0; 'EMPLOYED' = 1; 'TRAINING' = 2; 
 *  - 'SIGNED' = 3; 'CONFIRMED' = 4; 'MARKETING' = 5</li>
 *  </ul>
 * @author  Mason Wegert
 * @author  Diego Gomez
 * @author  Tim Law
 * @author  Pil Ju Chun
 **/

// Read the input file in as a spark Dataset<Row> with no header, therefore the
// resulting table column names are in the format _c#.

public class Driver {

	private static BufferedWriter writer, controlWriter;
	private static JavaSparkContext context;
	private static SparkSession session;

	/**
	 * This method creates the spark context and session and reads the input value.
	 * Data flow includes splitting control and model data, training the model, 
	 * testing the model, and printing the results.
	 * @param args - 0 input file location, 1 is main output, 2 is model parameters output
	 */
	public static void main(String args[]) {
		// Configure spark, get session variable, declare Datasets		
		context = new JavaSparkContext(new SparkConf().setAppName("ChanceToFail"));
		context.setLogLevel("ERROR");
		session = new SparkSession(context.sc());
		Dataset<Row> csv,filtered_csv,controlData,modelData;

		// Read input csv and infer data types schema implicitly.
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

		OptimalPoint optimalPointwk1 = applyControl(controlRDD_wk1, accuracyDelta, 1);
		OptimalPoint optimalPointwk2 = applyControl(controlRDD_wk2, accuracyDelta, 2);
		OptimalPoint optimalPoint = applyControl(controlRDD, accuracyDelta, 3);
		
		try {
			controlWriter.append("\nWeek 1 control data\nID, Drop chance, Actual status, Prediction\n");
			writeControlOutput(controlRDD_wk1, optimalPointwk1.getOptimalPercent());
			controlWriter.append("\nWeek 1-2 control data\nID, Drop chance, Actual status, Prediction\n");
			writeControlOutput(controlRDD_wk2, optimalPointwk2.getOptimalPercent());
			controlWriter.append("\nWeek 1-3 control data\nID, Drop chance, Actual status, Prediction\n");
			writeControlOutput(controlRDD, optimalPoint.getOptimalPercent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// TODO
		// calculate/print evaluation metrics
		// Assumed controlRDD is testing test data with model already applied to third column
		System.out.println("Mean Absolute Error: " + EvaluationMetrics.testMAE(controlRDD));
		System.out.println("Root Mean Squared Error: " + EvaluationMetrics.testRMSE(controlRDD));

		JavaPairRDD<Integer, Row> appliedResultPair = ModelApplier.applyModel(csv, modelParams);

		appliedResultPair.foreach(pairTuple -> {
			String prediction = pairTuple._2.getDouble(1)/pairTuple._2.getDouble(2) >= optimalPoint.getOptimalPercent() ? "DROP" : "PASS";
			// ID | aggregate drop chance | sum of r^2's | week # 
			writer.append(pairTuple._1 + "," + pairTuple._2.getDouble(1)/pairTuple._2.getDouble(2) + "," + pairTuple._2.getInt(4) + "," + prediction + "\n");
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
	private static void initWriters(String mainPath, String controlPath) {
		try {
			writer = new BufferedWriter(new FileWriter(mainPath, false));
			writer.append("battery_id,% Chance to Fail,Most Recent Week,Prediction\n");
			controlWriter = new BufferedWriter(new FileWriter(controlPath, false));
			controlWriter.append("--------Control data statistics--------\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints the equation for calculating the probability of failure based on the modelParams.
	 * Prints to the console and accuracyWriter for each of the 3 exam types (one for verbal, exam, project scores).
	 * Test type 4 (other) has a low correlation (0.2) and negatively effects the results.
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
	 * Writes the controlRDD output to the accuracy writer. If a row's drop % is greater than
	 * dropPercent it writes 'DROP', otherwise it writes 'PASS' at the end of the line.
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
	/**
	 * Finds the drop % cutoff point where the number of incorrect guesses is minimized
	 * @param controlRDD
	 * @param accuracyDelta
	 * @param weekNum
	 * @return
	 */
	private static OptimalPoint applyControl(JavaRDD<Row> controlRDD, double accuracyDelta, int weekNum) {
		controlRDD.cache();
		OptimalPoint optimalPoint = ModelApplier.findOptimalPercent(controlRDD, accuracyDelta);
		
		writeToControl("Fail percent: " + Math.round(optimalPoint.getOptimalPercent()*10000)/10000.0 + "\nCorrect estimates: " + 
				optimalPoint.getOptimalAccurateCount() + "\nTotal Count: " + controlRDD.count() + "\nAccuracy: " + 
				(double) optimalPoint.getOptimalAccurateCount()/(double)controlRDD.count() + "\n\n", weekNum);

		controlRDD.unpersist();
		return optimalPoint;
	}

	/**
	 * a simple writer class to the accuracyWriter and console, outString gets written/appended.
	 * @param outString
	 */
	private static void writeToControl(String outString, int weekNum) {
		try {
			controlWriter.append("\nAccuracy based on exams limited to week " + weekNum + "\n");
			System.out.println("\nAccuracy based on exams limited to week " + weekNum + "\n");
			
			System.out.println(outString);
			controlWriter.append(outString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}