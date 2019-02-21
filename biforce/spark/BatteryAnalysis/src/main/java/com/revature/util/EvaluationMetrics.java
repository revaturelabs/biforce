package com.revature.util;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Row;

// pass in JavaRDD<Row> as method arguments
// 1st column is battery id
// 2nd column should be % chance to fail (0.0 = 0% to 1.0 = 100%)
// 3rd column should be either 0 for fail or 1 for pass
public class EvaluationMetrics {

	// Return MAE (Mean Absolute Error)
	// EX1: if chance to fail is 0.70 but battery passed with 1, absolute error is
	// 0.70.
	// EX2: if chance to fail is 0.70 but battery failed with 0, absolute error is
	// 0.30.
	public static double testMAE(JavaRDD<Row> results) {
		return results.mapToDouble(row -> Math.abs(new Double(row.getInt(2)) - 1.0d + row.getDouble(1))).mean();
	}

	// Calculate MAE But after filtering out results within a user-defined
	// lower/upper bound.
	// bounds[0] = lower bound
	// bounds[1] = upper bound
	public static double testMAE(JavaRDD<Row> results, double[] bounds) {
		JavaRDD<Row> boundedResults = results
				.filter(row -> (row.getDouble(1) < bounds[0] || row.getDouble(1) > bounds[1]));
		return boundedResults.mapToDouble(row -> Math.abs(new Double(row.getInt(2)) - 1.0d + row.getDouble(1))).mean();
	}

	// Other MAE ideas: Automatically find optimal upper bound for "optimal" MAE
	// (This would answer business question of
	// "At what % fail percent should an associate be dropped")
//	public static double[] optimalBounds()
//	{
//		return new double[2];
//	}

	// Return RMSE (Root Mean Squared Error)
	public static double testRMSE(JavaRDD<Row> results) {
		return Math.sqrt(results
				.mapToDouble(row -> Math.pow((new Double(row.getInt(2)) - 1.0d + row.getDouble(1)), 2.0d)).mean());
	}

	// Calculate MAE But after filtering out results within a user-defined
	// lower/upper bound.
	// bounds[0] = lower bound
	// bounds[1] = upper bound
	public static double testRMSE(JavaRDD<Row> results, double[] bounds) {
		JavaRDD<Row> boundedResults = results
				.filter(row -> (row.getDouble(1) < bounds[0] || row.getDouble(1) > bounds[1]));
		return Math.sqrt(boundedResults
				.mapToDouble(row -> Math.pow((new Double(row.getInt(2)) - 1.0d + row.getDouble(1)), 2.0d)).mean());
	}
}