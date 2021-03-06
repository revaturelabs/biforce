package com.revature.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import scala.Tuple2;

/**
 * Takes an input csv, and split count and returns percentiles which are
 * equi-distant in the dataset for test types 0-3 inclusive.
 * 
 * @author Tim Law
 * @author Mason Wegert
 *
 */
public class PartitionFinder {
	/**
	 * splits up the csv by the percentiles which are equi-distant for the
	 * spitCount.
	 * 
	 * @param csv input csv
	 * @param splitCount - number of splits/buckets
	 * @return list of lists with percentiles for each test type
	 */
	public static List<List<Double>> read(Dataset<Row> csv, int splitCount) {
		Dataset<Row> validData = csv.drop("_c0","_c2","_c5","_c6","_c7","_c8").sort("_c3").persist();
		
		List<List<Double>> output = new ArrayList<>();
		System.out.println("Calculating Percentiles...");
		
		// each test type 1-3
		for (int i = 1; i <= 3; i++) {
			List<Double> percentiles = new ArrayList<>();

			Dataset<Row> weekX = validData.filter("_c1 = " + i);
			long totalNum = weekX.count();

			JavaPairRDD<Row, Long> withIndex = weekX.javaRDD().zipWithIndex();
			JavaPairRDD<Long, Row> indexKey = withIndex.mapToPair(k -> new Tuple2<Long, Row>(k._2, k._1()));

			indexKey.cache();
			for (long j = 1; j < splitCount; j++) {
				percentiles.add(indexKey.lookup(j * totalNum / splitCount).get(0).getDouble(1));
			}
			indexKey.unpersist();
			output.add(percentiles);
			System.out.println("Scores by percentile for test type " + i + ":" + percentiles);
		}
		validData.unpersist();
		return output;
	}
}
