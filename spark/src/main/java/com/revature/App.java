package com.revature;

import com.revature.sql.BatteryTestRegression;
import org.apache.spark.sql.SparkSession;

/**
 * Hello world!
 */
public class App {

  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: <input file> <output dir>");
      System.exit(1);
    }

    final String inputPath = args[0];
    final String outputPath = args[1];

    SparkSession spark = SparkSession
        .builder()
        .appName("Battery Analyzer")
        .master("local[*]")
        .getOrCreate();

    new BatteryTestRegression().execute(spark, inputPath, outputPath);
  }
}
