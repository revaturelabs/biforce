package com.revature.mapper;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.revature.util.Masker;

/**
 * The BatteryMapper class is responsible for writing to the intermediate output a desired Key-Value pair and masking test_type. 
 *
 * @author Bryn Portella, Daniel Campos-Bravo, Evan Diamond, & Jesse Hummel
 * @version 1.0
 * @since 2018-11-30
 */
public class BatteryMapper extends Mapper<LongWritable, Text, Text, Text> {

	/***
	 * The map method takes in single lines from a CSV file and parses each line using its comma delimiter. The delimited values are stored
	 * in an array of String and manipulated to match the desired Key-Value output. The form of this output is as follows:
	 *
	 * Key, as a single tab-delimited Text: battery_id, battery_group, battery_status, test_type
	 * Value, as a single tab-delimited Text: test_period, score
	 * 
	 * Note: test_type is masked.
	 * 
	 * @param LongWritable key This receives the unique key associated with each record, where a record is a single line.
	 * @param value This receives a record, where a record is a single line.
	 * @param context This receives the environment for running the operations.
	 * @return void 
	 * @exception throws IOException on input error.
	 * @exception throws InterruptedException on thread interruption.
	 * @see IOException
	 * @see InterruptedException
	 */
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String delimiter = "\t";
		
		String[] record = value.toString().split(",");
		if (record.length < 7) return;
		
		int batteryIdIndex = 0;
		int batteryGroupIndex = 1;
		int testTypeIndex = 2;
		int testPeriodIndex = 3;
		int scoreIndex = 4;
		int statusIndex = 6;

		String batteryStatus = Masker.mask(record[statusIndex]);
		String testTypeString = record[testTypeIndex];
		String testTypeMasked = "";

		switch(testTypeString){
		case "Verbal": testTypeMasked = "isVVV";
			break;
		case "Exam":testTypeMasked = "isEEE";
			break;
		case "Project": testTypeMasked = "isPPP";
			break;
		case "Other": testTypeMasked = "isOTH";
			break;
		default: 
		}


		Text returnKey = new Text(record[batteryIdIndex] + delimiter + record[batteryGroupIndex] +
									delimiter + batteryStatus+ delimiter + testTypeMasked);
		Text returnValue = new Text(record[testPeriodIndex] + delimiter + record[scoreIndex]);

		context.write(returnKey, returnValue);
	}
}