package com.revature.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import scala.Serializable;
import scala.Tuple2;

public class ModelApplier implements Serializable{
	private BufferedWriter writer;
	private BufferedWriter accuracyWriter;
	
	public ModelApplier(String mainFile, String controlFile) {
		try {
			writer = new BufferedWriter(new FileWriter(mainFile, false));
			accuracyWriter = new BufferedWriter(new FileWriter(controlFile, false));

			writer.append("battery_id,% Chance to Fail,Prediction\n");
			accuracyWriter.append("Control data statistics\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static final long serialVersionUID = -6089935981963552584L;

	public void writeControlOutput(JavaRDD<Row> controlRDD, double dropPercent) {
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

	public void applyModel(Dataset<Row> csv, double[][] bin1, double dropPercent) {
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

		sums.foreach(pairTuple -> {
			String prediction = pairTuple._2.getDouble(1)/pairTuple._2.getDouble(2) >= dropPercent ? "DROP" : "PASS";
			String s = pairTuple._1 + "," + pairTuple._2.getDouble(1)/pairTuple._2.getDouble(2) + "," + prediction + "\n";
			writer.append(s);
		});
	}

	public JavaRDD<Row> applyControlModel(Dataset<Row> csv, double[][] bin1) {
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

	public double findOptimalPercent(JavaRDD<Row> controlRDD, double[][] bin1, double controlPrecision) {
		double optimalPercent = 0;
		long optimalAccurateCount = 0;
		List<Double> percentList = new ArrayList<>();

		for (Double d = 0.0;d <= 1; d+= controlPrecision/100) {
			percentList.add(d);
		}

		controlRDD.cache();

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


		System.out.println("Fail percent: " + Math.round(optimalPercent*10000)/10000.0 + "\nCorrect estimates: " + optimalAccurateCount +
				"\nTotal Count: " + controlRDD.count() + "\nAccuracy: " + optimalAccurateCount/controlRDD.count() + "\n");

		try {
			accuracyWriter.append("Fail percent: " + Math.round(optimalPercent*10000)/10000.0 + "\nCorrect estimates: " + optimalAccurateCount +
					"\nTotal Count: " + controlRDD.count() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		controlRDD.unpersist();
		return optimalPercent;
	}

	public void close() {
		try {
			writer.close();
			accuracyWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}