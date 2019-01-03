package com.revature.reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * The BatteryOutlierReducer class identifies and isolates batteries with an abnormal number of isAAA tests
 * 
 * @author Evan Diamond & Bryn Portella
 * @version 1.0
 * @since 2018-11-30
 */
public class BatteryOutlierReducer extends Reducer<Text, Text, Text, Text> {

	private static String[] testTypes = {"isAAA", "isPPP", "isVVV", "isEEE"};
	
	/***
	 * The reduce method takes in the group_id as a single Text key,
	 * and an Iterable of Text elements that are composed of battery_id, battery_status, and the test_type and number of scores for that type.
	 * The method finds the mode number of isAAA tests for the group, and identifies batteries with a different number of tests
	 *
	 * @param Text key This receives the group_id as a single Text key.
	 * @param values This receives  an Iterable of Text elements that are composed of battery_id, 
	 * 			battery_status, and the test_type and number of scores for that type
	 * @param context This receives the environment for running the operations.
	 * @return void 
	 * @exception throws IOException on input error.
	 * @exception throws InterruptedException on thread interruption.
	 * @see IOException
	 * @see InterruptedException
	 */
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		Map<String, List<Integer>> testCounts= new HashMap<>();
		for (String testType : testTypes){
			testCounts.put(testType, new ArrayList<Integer>());
		}
		Map<String, Map<String, Integer>> batteryCounts= new HashMap<>();	//batteryCounts maps Battery ID to the counts map, described below, for each battery
		for (Text value : values) {
			String[] records = value.toString().split(":");				//each element of records is one test type and the number of tests 
			String batteryId = records[0];
			Map<String, Integer> counts = new HashMap<>();				//counts maps test type to number of tests performed, for one battery
			for(int i = 1; i<records.length; i++){
				String[] line = records[i].split("\t");
				String type = line[0];
				int count = Integer.parseInt(line[1]);
				counts.put(type, count);
				testCounts.get(type).add(count);
			}
			batteryCounts.put(batteryId, counts);
		}	
		Map<String, Integer> modes = new HashMap<>();					//modes maps test type to mode number of scores in that type across the batch
		for (String type: testCounts.keySet()){
			modes.put(type, mode(testCounts.get(type)));
		}
		for (String ID : batteryCounts.keySet()){						//Writes to output batteries that had a number of isAAA tests different from the group mode
			try{
			if (!batteryCounts.get(ID).get("isAAA").equals(modes.get("isAAA"))){
				context.write(new Text(ID + "\t" + key.toString()), new Text(batteryCounts.get(ID).get("isAAA") + "\t" + modes.get("isAAA")));
			}
			} catch (NullPointerException e){}
		}
	}
	
	/**
	 * Calculates the mode of a list of Integers
	 * @param list List of Integers to find the mode of
	 * @return int of the mode of list
	 */
	public static int mode(List<Integer> list){
		Map<Integer, Integer> counts = new HashMap<>();
		for (Integer number : list){
			Integer count = counts.get(number);
			if (count != null){ 
				counts.put(number, count+1);
				}
			else counts.put(number, 1);
		}
		try {
			Integer max = Collections.max(counts.values());
			for (Integer num : counts.keySet()){
				if(counts.get(num).equals(max)){
					return num;
				}
			}
		} catch (NoSuchElementException e){}
		return 0;
	}
}
