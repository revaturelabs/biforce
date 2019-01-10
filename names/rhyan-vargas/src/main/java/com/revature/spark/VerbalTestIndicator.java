package com.revature.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class VerbalTestIndicator {
	
	public AnalyticResult execute(Dataset<Row> csv, Row inputRow, int period) {
		double score, scoreLowerBound, scoreUpperBound, outputPercentage = 0;
		long totalAmount, failedAmount;
		
		csv = csv.filter("_c0 = " + period + " AND _c3 = 1 AND (_c9 = 1 OR _c9 = 2)");
		
		score = Double.parseDouble(inputRow.get(2).toString());
		scoreLowerBound = score-10;
		scoreUpperBound = score+10;
		
		Dataset<Row> csvTotal = csv.filter("_c2 >= " + scoreLowerBound + " AND _c2 <= " + scoreUpperBound);
		totalAmount = csvTotal.groupBy("_c8").count().distinct().count();
		failedAmount = csvTotal.filter("_c9 = 1").groupBy("_c8").count().distinct().count();
		if (totalAmount!=0)
			outputPercentage = (double)failedAmount/(double)totalAmount*100;
		
		return new AnalyticResult(outputPercentage, (int)totalAmount, "Result is based on those who scored "
				+ "similarly(+/- 10) on verbal tests taken in period " + period);
	}
}
