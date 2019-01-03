package com.revature.reducer;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @Deprecated as of 11-29-18
 * @author Evan Diamond
 *
 */
public class BatteryTestCountReducer extends Reducer<Text, Text, Text, Text> {
	
	/**
	 * Reducer that counts number of tests of each type for each battery
	 * @Deprecated as of 11-29-18
	 */
	@Override
	@Deprecated public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		StringBuilder output = new StringBuilder();
		for (Text value : values) {
			String[] records = value.toString().split(":");				//each element of records is one test type and the number of tests 
			int v, p, e, a;
			v = p = e = a = 0;
			StringBuilder battery = new StringBuilder(records[0]);		 
			for(int i = 1; i<records.length; i++){
				String[] line = records[i].split("\t");
				String type = line[0];
				int count = Integer.parseInt(line[1]);
				switch(type){
				case("isAAA"): a = count;								//match the count to the test type it is associated with
				break;
				case("isEEE"): e = count;			
				break;
				case("isPPP"): p = count;
				break;
				case("isVVV"): v = count;
				break;

				}
			}
			battery.append("\t"+v);
			battery.append("\t"+e);
			battery.append("\t"+p);
			battery.append("\t"+a);
			output.append(battery.toString() + ",");
		}
		//key: group id; value: battery id		# of v tests		# of e tests	# of p tests	# of a tests
		context.write(key, new Text(output.substring(0,output.length()-1)));	
	}
}