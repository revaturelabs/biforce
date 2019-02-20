package com.revature.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import scala.Tuple2;

public class ModelApplier {
	
	public static JavaPairRDD<Integer, Row> applyModel(Dataset<Row> csv, double[][] bin1, double dropPercent) {
		JavaRDD<Row> csvRDD = 
				csv
				.javaRDD()
				.filter(row -> row.getInt(1) != 4) // get rid of test 4, it's too inaccurate
				.map(row->{
					// row should have 13 cols now. Col 0-10 as before, col 11 as "result", col 12 as "weight"
					double failPercent = 0;
					double rValue = 0;
					for (int i=1;i < 4;++i) {
						if (row.getInt(1) == i) { // Assessment type 1-3
							failPercent = Math.exp(row.getDouble(3) * bin1[i-1][1] + bin1[i-1][2])/
									(1 + Math.exp(row.getDouble(3) * bin1[i-1][1] + bin1[i-1][2])) *
									bin1[i-1][3];
							rValue = bin1[i-1][3];
							break;
						}
					}
					// row: (int, int, int, double, int, int, int, int, int, int, int)
					Row outputRow = RowFactory.create(row.getInt(9), failPercent, rValue);
					return outputRow;
				});

		JavaPairRDD<Integer,Row> applicationRDD = csvRDD.mapToPair(row -> new Tuple2<Integer,Row>(row.getInt(0),row));
		JavaPairRDD<Integer, Row> sums = applicationRDD.reduceByKey((Row row1,Row row2)->{
			return RowFactory.create(row1.getInt(0), row1.getDouble(1) + row2.getDouble(1), row1.getDouble(2) + row2.getDouble(2));});
		
		return sums;
	}

	public static JavaRDD<Row> applyControlModel(Dataset<Row> csv, double[][] bin1) {
		JavaRDD<Row> csvRDD = 
				csv
				.javaRDD()
				.filter(row -> row.getInt(1) != 4) // get rid of test 4, it's too inaccurate
				.map(row->{
					// row should have 13 cols now. Col 0-10 as before, col 11 as "result", col 12 as "weight"
					double failPercent = 0;
					double rValue = 0;

					for (int i=1;i < 4;++i) {
						if (row.getInt(1) == i) { // Assessment type 1-3
							failPercent = Math.exp(row.getDouble(3) * bin1[i-1][1] + bin1[i-1][2])/
									(1 + Math.exp(row.getDouble(3) * bin1[i-1][1] + bin1[i-1][2])) *
									bin1[i-1][3];
							rValue = bin1[i-1][3];
							break;
						}
					}

					int status = row.getInt(10) == 0 ? 0 : 1;

					// row: (int, int, int, double, int, int, int, int, int, int, int)
					Row outputRow = RowFactory.create(row.getInt(9), failPercent, rValue, status);
					return outputRow;
				});

		JavaPairRDD<Integer,Row> applicationRDD = csvRDD.mapToPair(row -> new Tuple2<Integer,Row>(row.getInt(0),row));
		JavaPairRDD<Integer, Row> sums = applicationRDD.reduceByKey((Row row1,Row row2)->{
			return RowFactory.create(row1.getInt(0), row1.getDouble(1) + row2.getDouble(1), row1.getDouble(2) + row2.getDouble(2), row1.getInt(3));});

		return sums.map(pairTuple -> {
			// Associate ID | % failure | fail (1 or 0)
			return RowFactory.create(pairTuple._1, pairTuple._2.getDouble(1)/pairTuple._2.getDouble(2), pairTuple._2.getInt(3));
		});
	}
	
	public static OptimalPoint findOptimalPercent(JavaRDD<Row> controlRDD, double controlPrecision) {
		List<Double> percentList = new ArrayList<>();
		long optimalAccurateCount = 0;
		double optimalPercent = 0;
		
		for (Double d = 0.0;d <= 1; d+= controlPrecision/100) {
			percentList.add(d);
		}
		
		for (int i = 0;i<percentList.size();++i) {
			// controlRDD: Associate ID | % failure | fail (0 or 1 where 0 is fail)
			double d = percentList.get(i);

			// Filter out those who passed, and those who failed but we guessed wrong
			long accurateFailedCount = controlRDD.filter(row -> row.getInt(2) == 0 && row.getDouble(1) >= d).count();
			// Filter out those who failed, and those who passed but we guessed wrong
			long accuratePassedCount = controlRDD.filter(row -> row.getInt(2) != 0 && row.getDouble(1) < d).count();
			long accurateCount = accurateFailedCount + accuratePassedCount;

			if (accurateCount > optimalAccurateCount) {
				optimalAccurateCount = accurateCount;
				optimalPercent = percentList.get(i);
			}
		}
		return new OptimalPoint(optimalPercent,optimalAccurateCount, controlRDD.count());
	}
}
