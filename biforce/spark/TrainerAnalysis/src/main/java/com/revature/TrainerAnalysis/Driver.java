package com.revature.TrainerAnalysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;


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
 * @author  Austin Hunter
 * @author  Garett Nguyen
 * @author  Jason Douglas
 * @author  Sean Tesch
 **/

// Read the input file in as a spark Dataset<Row> with no header, therefore the
// resulting table column names are in the format _c#.

public class Driver {
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
		String s3Location = "s3://revature-analytics-dev/";

		context = new JavaSparkContext(new SparkConf().setAppName("ChanceToFail"));
		context.setLogLevel("ERROR");
		session = new SparkSession(context.sc());
		Dataset<Row> csv, filtered_csv, controlData, modelData;
		JavaRDD<String> controlOutput;

		// Read input csv and infer data types schema implicitly.
		csv = session.read().format("csv").option("header", "false").option("inferSchema", "true").load(args[0]);
	}
	private static JavaRDD<String> writeOutput(JavaPairRDD<Integer, Row> appliedResultPair, double dropPercent) {
		return appliedResultPair.map(pairTuple -> {
			/* make own implementation
			String prediction = pairTuple._2.getDouble(1)/pairTuple._2.getDouble(2) >= dropPercent ? "DROP" : "PASS";
			if (Double.isNaN(pairTuple._2.getDouble(1)/pairTuple._2.getDouble(2))) prediction = "UNK";
			// ID | chance to fail | most recent week | prediction 
			return pairTuple._1 + "," + pairTuple._2.getDouble(1) / pairTuple._2.getDouble(2) + ","
			+ pairTuple._2.getInt(4) + "," + prediction;
			*/
		});
	}
}
	