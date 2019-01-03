package com.revature.reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * The BatteryMedianCombiner class calculates the median score in each test type for individual batteries
 * 
 * @author Evan Diamond & Bryn Portella
 * @version 1.0
 * @since 2018-11-30
 */
public class BatteryMedianCombiner extends Reducer<Text, Text, Text, Text> {
	/***
	 * The reduce method takes in the tab-delimited battery_id, and status as a single Text key,
	 * and an Iterable of Text elements that are composed of tab-delimited group_id, test_type, test_scores.
	 * The method calculates the batery's median score in each test type, and also filters out batteries that
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
			groupID = record[0];
			String type = record[1];
			if (record.length >= 4) toWrite = true;		//Only writes in battery has at least two scores in any test type 
			List<Double> scores = parseList(Arrays.asList(Arrays.copyOfRange(record, 2, record.length)));
			Double median = median(scores);				//Calculates battery's median score
			returnStr += ":" + type + "\t" + median;
		}
		Text returnKey = new Text(groupID);				//key: Group ID
		Text returnValue = new Text(returnStr);			//value: battery id \t status : test type \t median : test type \t median : ...
		if (toWrite){
			context.write(returnKey, returnValue);
		}
	}
	
	/**
	 * Parses a List of strings as an ArrayList of Doubles, ignoring elements for which such a parsing is impossible
	 * @param stringList List of string to be parsed
	 * @return	ArrayList of Doubles parsed from strings
	 */
	public static ArrayList<Double> parseList(List<String> stringList){
		ArrayList<Double> DoubleList = new ArrayList<Double>();
		for(String entry : stringList){
			try{
				DoubleList.add(Double.parseDouble(entry));
			}catch (NumberFormatException e) {}
		}
		return DoubleList;
	}
	
	/**
	 * Calculates the median of a List of Doubles
	 * @param list List of Doubles to be aggregated
	 * @return median of the list
	 */
	public static Double median(List<Double> list){
		Double median;
		list.sort(null);
		if (list.size()%2 == 0){
			try{
				median = ((list.get(list.size()/2)+list.get((list.size()/2)-1))/2);
			}catch (NullPointerException e){
				median = 0.0;
			}catch (IndexOutOfBoundsException e){
				median = 0.0;
			}
		}else{
			median = list.get((list.size()-1)/2);
		}
		return median;
	}
}
