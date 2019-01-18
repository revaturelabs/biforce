package com.revature.spark;

import java.util.List;

import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
/*
 * Code by: Sarah Faust
 * 
 * This class filters the given dataset to only include batteries with a definitive pass/fail status.
 * These batteries and the patterns of their test results will be used to set the standard as to whether other batteries
 * 		will pass or not based on how their test results compare.
 * 
 * The output will have:
 * 	A) The first 3 test periods of batteries with a battery_status of 1
 * 	B) The first 3 test periods of batteries with a battery_status of 2
 * 	C) The first 3 test periods of batteries with a battery_status of 3 AND feature a test_period of 9 or higher
 * 
 * Note that entire rows are outputted, not just specific columns.
 */

/*
 * for each row:
 *	test type, raw score, score, test period, test catagory, builder id, group id, group type, battery id, battery status
 *
 * battery_status: dropped = 1, employed = 2, training = 3 (ignore others)
 */

// "_c#" refers to the column names - the default names provided by spark.
public class PassFailSampleFilter {
	
	// input: the full table of data given to us (battery_test.csv), output file path.
	public static Dataset<Row> execute(Dataset<Row> csv) {
		
		
		// Get a list of battery_ids that match the criteria, and drop all columns except that which lists the battery IDs.
		Dataset<Row> filteredCSV = csv.filter("_c9 = 0 OR _c9 = 1 OR (_c9 = 3 AND (_c3 = 9 OR _c3 = 10))")
				.drop("_c0", "_c1","_c2","_c3","_c4","_c5","_c6","_c7","_c9","_c10").dropDuplicates();
		
		// Convert the column of battery IDs to a list and convert that list to a string.
		List<Row> battery_IDs = filteredCSV.collectAsList();
		StringBuilder listOfIDs = new StringBuilder("");
		for(Row r:battery_IDs) {
			listOfIDs.append(" "+r.mkString());
		}
		String batteryList = listOfIDs.toString();
		
		// Then filter out all records of batteries whose ID isn't present within the string of approved battery IDs.
		// Also only keep records covering the first three test periods of the approved battery IDs.
		csv = csv.filter((FilterFunction<Row>)row -> {
			return (batteryList.contains(row.get(8).toString()) && (
					Integer.parseInt(row.get(3).toString())==3 ||
					Integer.parseInt(row.get(3).toString())==2 ||
					Integer.parseInt(row.get(3).toString())==1
					));
		});
		
		return csv;
	}
}
