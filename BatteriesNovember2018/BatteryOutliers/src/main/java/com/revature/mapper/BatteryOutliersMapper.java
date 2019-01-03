package com.revature.mapper;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * The BatteryOutliersMapper class is responsible for writing to the combiner a desired Key-Value pair. Additionally,
 * the mapper eliminates the unneeded "isOTH" test type.
 *
 * @author Evan Diamond & Daniel Campos-Bravo
 * @version 1.0
 * @since 2018-11-30
 */
public class BatteryOutliersMapper extends Mapper<LongWritable, Text, Text, Text> {

	/***
	 * The map method takes in single lines from an input file and parses each line using its tab delimiter. The delimited values are stored
	 * in an array of String and manipulated to match the desired Key-Value output. The form of this output is as follows:
	 *
	 * Key, as a single tab-delimited Text: battery_id, battery_status,
	 * Value, as a single tab-delimited Text: group_id, test_type, scores
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
		String[] record = value.toString().split("\t");													
		Arrays.toString(record);
		String testValues = "";	
		try {
			if (!record[3].equals("isOTH")){																// Insures records with isOTH do not get evaluated.
				for (int i = 4; i < record.length; i++)
					testValues += record[i] + "\t";															// Populates testValues as a String of record values delimited by tab.

				Text returnKey = new Text(record[0] + "\t" + record[2]);									//Key: Battery id \t status
																											//Value: Group id \t test type \t scores
				Text returnValue = new Text(record[1] + "\t" + record[3] + "\t" + testValues.substring(0, testValues.length()-1));				

				context.write(returnKey, returnValue);
			}
		}catch (ArrayIndexOutOfBoundsException ex){}
	}
}