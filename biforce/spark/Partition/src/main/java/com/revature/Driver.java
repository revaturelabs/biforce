package com.revature;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import com.revature.util.PartitionFinder;

public class Driver {
	public static SparkSession session;
	public static JavaSparkContext context;
	
	public static void main(String[] args) {
		if (args.length!=2) {
			System.out.println("Need 3 input arguments");
			System.exit(1);
		}
		final String SPARK_MASTER = args[0];
		final String INPUT_PATH = args[1];
		// Set Spark configuration for Context.
		SparkConf conf = new SparkConf().setAppName("Partition").setMaster(SPARK_MASTER);
		context = new JavaSparkContext(conf);
		context.setLogLevel("ERROR");
		session = new SparkSession(context.sc());
		// Read the input file in as a spark Dataset<Row> with no header, therefore the
		// resulting table column names are in the format _c#. InferSchema to interprets
		// column data type and optimize storage/processing data.
		Dataset<Row> csv = session.read().format("csv").option("header","false").option("inferSchema", "true").load(INPUT_PATH);
		PartitionFinder pf = new PartitionFinder();
		pf.read(csv);
	}
}
