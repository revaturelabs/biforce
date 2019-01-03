package com.revature.reducer;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * The BatteryTestCountsCombiner class calculates the number of scores in each test type for individual batteries
 * 
 * @author Evan Diamond
 * @version 1.0
 * @since 2018-11-30
 */
public class BatteryTestCountsCombiner extends Reducer<Text, Text, Text, Text> {

	/***
	 * The reduce method takes in the tab-delimited battery_id, and status as a single Text key,
	 * and an Iterable of Text elements that are composed of tab-delimited group_id, test_type, test_scores.
	 * The method calculates the number of scores the battery has in each test type, and also filters out batteries that
	 * were not present through at least 2 testing periods.
	 * It writes its output using the battery's group id as the key, such that the Reducer can sort by group.
	 *
	 * @param Text key This receives a tab-delimited battery_id, and status as a single Text key.
	 * @param values This receives an Iterable of Text elements that are composed of tab-delimited group_id, test_type, test_scores
	 * @param context This receives the environment for running the operations.
	 * @return void 
	 * @exception throws IOException on input error.
	 * @exception throws InterruptedException on thread interruption.
	 * @see IOException
	 * @see InterruptedException
	 */
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String groupID = "";
		String returnStr = key.toString();
		boolean toWrite = false;
		for (Text value : values) {
			String[] record = value.toString().split("\t");
			if (record.length >= 4) toWrite = true;
			groupID = record[0];
			String type = record[1];
			int count = 0;
			for(String datapoint: record){						//only counts non-null datapoints
				if (datapoint.length()>0) count++;
			}
			returnStr += ":" + type + "\t" + (count-2); 		//appends test type and number of those tests, delimited by :
		}
		Text returnKey = new Text(groupID);						//key: Group ID
		Text returnValue = new Text(returnStr);					//value: battery id \t status: test type \t count : test type \t count : ...
		if (toWrite) {
			context.write(returnKey, returnValue);
		}
	}
}