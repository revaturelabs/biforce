package com.revature.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import scala.Tuple2; 

// Tim Law & Mason Wegert

public class PartitionFinder {
	public static List<List<Double>> read(Dataset<Row> csv) {
//		Dataset<Row> csv2 = csv.drop("_c0","_c2","_c5","_c6","_c7","_c8"); //1,3,4,9,10
//		Dataset<Row> definitiveTrainees = csv2.filter("_c10 = 0 OR _c10 = 1 OR _c4 = 8 OR _c4 = 9").dropDuplicates();
//		Dataset<Row> valid1 = csv2.join(definitiveTrainees,csv2.col("_c9").equalTo(definitiveTrainees.col("_c9")),"left_semi").filter("_c4<=3");
//		Dataset<Row> sufficientDataTrainees = csv2.filter("_c4>2").dropDuplicates();
//		Dataset<Row> valid2 = valid1.join(sufficientDataTrainees, valid1.col("_c9").equalTo(sufficientDataTrainees.col("_c9")),"left_semi");
		Dataset<Row> valid2 = csv.drop("_c0","_c2","_c5","_c6","_c7","_c8");
		
		List<List<Double>> output = new ArrayList<>();
		
		// each week 1-3
//		for (int i=1;i<=3;i++) {
//			List<Double> percentiles = new ArrayList<>();
//			
//			Dataset<Row> weekX = valid2.filter("_c4 = " + i).sort("_c3");
//			long totalNum = weekX.count();
//			
//			JavaPairRDD<Row, Long> withIndex = weekX.javaRDD().zipWithIndex();
//			JavaPairRDD<Long, Row> indexKey = withIndex.mapToPair(k -> new Tuple2<Long, Row>(k._2, k._1()));
//			
//			for (long j=1;j<10;j++) {
//				percentiles.add(indexKey.lookup(j * totalNum/10).get(0).getDouble(1));
//			}
//			output.add(percentiles);
//			System.out.println(percentiles);
//		}
		
		// each test type 1-3
		for (int i=1;i<=3;i++) {
			List<Double> percentiles = new ArrayList<>();
			
			Dataset<Row> weekX = valid2.filter("_c1 = " + i).sort("_c3");
			long totalNum = weekX.count();
			
			JavaPairRDD<Row, Long> withIndex = weekX.javaRDD().zipWithIndex();
			JavaPairRDD<Long, Row> indexKey = withIndex.mapToPair(k -> new Tuple2<Long, Row>(k._2, k._1()));
			
			for (long j=1;j<10;j++) {
				percentiles.add(indexKey.lookup(j * totalNum/10).get(0).getDouble(1));
			}
			output.add(percentiles);
			System.out.println(percentiles);
		}
		return output;
	}
}
