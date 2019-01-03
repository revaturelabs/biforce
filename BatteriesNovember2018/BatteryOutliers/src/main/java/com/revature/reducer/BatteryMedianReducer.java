package com.revature.reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import static com.revature.reducer.BatteryMedianCombiner.median;

/**
 * The BatteryMedianReducer class identifies and isolates batteries performing poorly in one or more test types, relative to the rest of their group
 * 
 * @author Evan Diamond & Bryn Portella
 * @version 1.0
 * @since 2018-11-30
 */
public class BatteryMedianReducer extends Reducer<Text, Text, Text, Text> {

	private static String[] testTypes = {"isAAA", "isPPP", "isVVV", "isEEE"};
	
	/***
	 * The reduce method takes in the group_id as a single Text key,
	 * and an Iterable of Text elements that are composed of battery_id, battery_status, and the test_type and median score for that type.
	 * The method calculates the overall median scores and median absoulte deviation (MAD) for the group in each test type,
	 * and finds batteries that underperform in one or more test types, defined as those whose median score is more that 1 MAD below the overall group median
	 *
	 * @param Text key This receives the group_id as a single Text key.
	 * @param values This receives  an Iterable of Text elements that are composed of battery_id, 
	 * 					battery_status, and the test_type and median score for that type.
	 * @param context This receives the environment for running the operations.
	 * @return void 
	 * @exception throws IOException on input error.
	 * @exception throws InterruptedException on thread interruption.
	 * @see IOException
	 * @see InterruptedException
	 */
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		Map<String, List<Double>> testMedians= new HashMap<>();			//testMedians maps test type to a list containing all batteries' medians for that test
		for (String testType : testTypes){
			testMedians.put(testType, new ArrayList<Double>());
		}
		Map<String, Map<String, Double>> batteryMedians= new HashMap<>();	//batteryMedians maps individual batteries to inner maps, 
		for (Text value : values) {											//which map test type to the battery's median score
			String[] records = value.toString().split(":");				//each element of records is one test type and the number of tests 
			String batteryId = records[0];
			Map<String, Double> medians = new HashMap<>();
			for(int i = 1; i<records.length; i++){
				String[] line = records[i].split("\t");
				String type = line[0];
				double median = Double.parseDouble(line[1]);
				medians.put(type, median);
				testMedians.get(type).add(median);
			}
			batteryMedians.put(batteryId, medians);
		}	
		Map<String, Double> mediansOfMedians = new HashMap<>();			//mediansOfMedians maps test type to median across all batteries in the group
		for (String type: testMedians.keySet()){
			mediansOfMedians.put(type, median(testMedians.get(type)));
		}
		Map<String, Double> deviations = new HashMap<>();				//deviations maps test type to median absolute deviation across the group in that test type 
		for (String type : mediansOfMedians.keySet()){
			deviations.put(type, medianAbsoluteDiviation(testMedians.get(type), mediansOfMedians.get(type)));
		}
		
		/*
		 * checks if a battery is performing at 1 MAD or more below the group median, 
		 * and if so, writes to output 
		 * Output key: Battery ID, Status, Group ID
		 * Output value: test types where the battery is underperforming along with it's median score vs that of the group
		 */
		for (String batteryID : batteryMedians.keySet()){
			StringBuilder batteryDeficientTests = new StringBuilder("");
			for (String type : deviations.keySet()){
				if (mediansOfMedians.get(type) > 0.0){
					try{
						Double batterysMed = batteryMedians.get(batteryID).get(type);
						Double groupMed = mediansOfMedians.get(type);
						Double groupDev = deviations.get(type);
						if (batterysMed < (groupMed - groupDev)){
							batteryDeficientTests.append(type + ": " + batterysMed + " vs " +groupMed + ";\t");
						}
					}catch (NullPointerException e){}
				}
			}
			if (batteryDeficientTests.length() > 1){
				context.write(new Text(batteryID + "\t" + key.toString()), new Text(batteryDeficientTests.toString()));
			}
		}
	}
	
	/**
	 * calculates the median absolute deviation of a list of Doubles from a single given double, 
	 * 
	 * @param list	List of Doubles to calculate over
	 * @param median Double that all doubles in list will be compared
	 * @return
	 */
	public static double medianAbsoluteDiviation(List<Double> list, Double median){
		List<Double> absoluteDeviations = new ArrayList<>();
		for (Double datapoint : list){
			absoluteDeviations.add(Math.abs(datapoint - median));
		}
		double mad = median(absoluteDeviations);
		return mad;
	}
}
