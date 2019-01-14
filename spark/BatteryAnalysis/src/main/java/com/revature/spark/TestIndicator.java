package com.revature.spark;

import java.io.Serializable;
import java.util.NoSuchElementException;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class TestIndicator implements Serializable {
	
	public AnalyticResult execute(Dataset<Row> csv, Dataset<Row> battery_id_tests, int period, int testType) {
		double score, scoreLowerBound, scoreUpperBound, outputPercentage = 0;
		long totalAmount, failedAmount;
		
		// Filter the input to include only those of the given test types and test periods.
		csv = csv.filter("_c0 = " + testType + " AND _c3 = " + period);
		battery_id_tests = battery_id_tests.filter("_c0 = " + testType + " AND _c3 = " + period);

		try {
			//Find the score the input battery id got on the given test.
			score = Double.parseDouble(battery_id_tests.first().get(2).toString());
		}
		catch(Exception e) {
			// If no score is found for the given test, return null, 
			// this indicator doesn't apply for this battery id.
			return null;
		}
		
		// Set the range to look at for the given score, we look at those who scored within 10 points
		// of the battery id we were given.
		scoreLowerBound = score-10;
		scoreUpperBound = score+10;
		

		//Filter the data set to find just those who scored similarly and passed or failed
		Dataset<Row> csvTotal = csv.filter("(_c9 = 1 OR _c9 = 2 OR _c9 = 3) AND _c2 >= " + scoreLowerBound + " AND _c2 <= " + scoreUpperBound);
		//Count the unique battery_ids of those who scored similarly
		totalAmount = csvTotal.groupBy("_c8").count().distinct().count();
		// Count the unique battery_ids of those who score similarly and just failed.
		failedAmount = csvTotal.filter("_c9 = 1").groupBy("_c8").count().distinct().count();
		// Calculate the percentage of those who failed.
		if (totalAmount!=0)
			outputPercentage = (double)failedAmount/(double)totalAmount*100;
		
		// Return an AnalyticResult that returns the % chance to fail, the sample size, and an explanation.
		return new AnalyticResult(outputPercentage, (int)totalAmount, "Result is based on those who scored "
				+ "similarly (" + score + " +/-10) on test type " + testType + " taken in period " + period);
	}
}
