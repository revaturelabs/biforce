package com.revature.reducer;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * The BatteryReducer class is responsible for receiving the intermediate output from BatteryMapper or the BatteryAAAMapper and providing the implementation for the reduce method.
 *
 * @author Bryn Portella, Evan Diamond, Daniel Campos-Bravo, Jesse Hummel, & Scott Marshall
 * @version 1.0
 * @since 2018-11-30
 */
public class BatteryReducer extends Reducer<Text, Text, Text, Text> {

	/***
	 * The reduce method takes in the tab-delimited battery_id, test_type, and status as a single Text key,
	 * and an Iterable of Text elements that are composed of tab-delimited test_scores and test_periods.
	 * Additionally, the method is responsible for identifying elements that contain "isPPP" as its test_type, and shifting said element's values 
	`* closer to its test_type. This is done to remove the null values that are in between the non-null values in order to make the element's
	 * valuable data more compact. This is done under the assumption that the test_period order for "isPPP" is not needed. Thus, the method
	 * returns its input key as its output key, and a perserved-test_period value, where test_type is not "isPPP," or a non-perserved-test_period value,
	 * where test_type is "isPPP."
	 *
	 * @param Text key This receives a tab-delimited battery_id, test_type, and status as a single Text key.
	 * @param values This receives an Iterable of Text elements that are composed of tab-delimited test_scores and test_periods.
	 * @param context This receives the environment for running the operations.
	 * @return void 
	 * @exception throws IOException on input error.
	 * @exception throws InterruptedException on thread interruption.
	 * @see IOException
	 * @see InterruptedException
	 */
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		String[] temp = new String[10];
		StringBuilder line = new StringBuilder("");
		String[] scores = new String[10];
		int testTypeIndex = 0;
		String keyString = key.toString();
		String testTypePPP = "isPPP";
		String delimiter = "\t";

		//Places scores in the scores array at the index corresponding to the period it was recorded
		for (Text value : values) {
			temp = value.toString().split(delimiter);
			try{
				int week = Integer.parseInt(temp[0]);
				String score = temp[1];

				scores[week-1] = score;
				
			}catch(NumberFormatException ex){
			}catch(NullPointerException ex){
			}catch(ArrayIndexOutOfBoundsException ex){}
			
		}
		
		//Compresses scores Array for isPPP test
		if(keyString.contains(testTypePPP)){
			int currentScoreIndex = 0;
			String[] tempScores = new String[10];
			for(String score: scores){
				if(score != null){
					tempScores[currentScoreIndex] = score;
					currentScoreIndex++;
				}
			}
			scores = tempScores;
		}

		//Adds scores to a StringBuilder line, and preserves nulls as blank spaces
		for(String score : scores){
			if (score == null){
				line.append(delimiter);
			}else{
				line.append(delimiter + score);
			}  
		}
		String output = line.substring(1);
		context.write(key, new Text(output));
	}
}
