package com.revature.spark;

import java.io.Serializable;
import java.util.NoSuchElementException;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class TestIndicator implements Serializable {
	
	public AnalyticResult execute(Dataset<Row> csv, int input_battery_id, int period, int testType) {
		double score, scoreLowerBound, scoreUpperBound, outputPercentage = 0;
		long totalAmount, failedAmount;
		
		csv = csv.filter("_c0 = " + testType + " AND _c3 = " + period);
		
		try {
			score = Double.parseDouble(csv.filter("_c8 = " + input_battery_id).first().get(2).toString());
		}
		catch(Exception e) {
			return null;
		}
		
		scoreLowerBound = score-10;
		scoreUpperBound = score+10;
		
		Dataset<Row> csvTotal = csv.filter("_c2 >= " + scoreLowerBound + " AND _c2 <= " + scoreUpperBound);
		totalAmount = csvTotal.groupBy("_c8").count().distinct().count();
		failedAmount = csvTotal.filter("_c9 = 1").groupBy("_c8").count().distinct().count();
		if (totalAmount!=0)
			outputPercentage = (double)failedAmount/(double)totalAmount*100;
		
		return new AnalyticResult(outputPercentage, (int)totalAmount, "Result is based on those who scored "
				+ "similarly (" + score + "+/-10) on test type " + testType + " taken in period " + period);
	}
}
