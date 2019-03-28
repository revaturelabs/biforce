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

import com.revature.Util.*;

public class DriverClass {
	public static void main(String[] args) {
		final String inputpath = args[0];
		final String outputpath = args[1];
		
		SparkConf conf = new SparkConf().setAppName("TrainerAnalysis");
		JavaSparkContext context = new JavaSparkContext(conf);
		
		new xxx.write.format("csv").save("s3://revature-analytics-dev");
		new yyy.write.format("csv").save("s3://revature-analytics-dev");
		new zzz.write.format("csv").save("s3://revature-analytics-dev");
		new aaa.write.format("csv").save("s3://revature-analytics-dev");
		new hhh.write.format("csv").save("s3://revature-analytics-dev");
		
	}
}
