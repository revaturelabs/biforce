package com.revature;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.revature.mapper.BatteryOutliersMapper;
import com.revature.reducer.BatteryTestCountsCombiner;
import com.revature.reducer.BatteryTestCountReducer;

/**
 * @Deprecated as of 11-29-18
 * @author Evan Diamond
 *
 */
public class BatteryTestCountsDriver {
	
	/**
	 * Driver to count number of tests of each type for each battery in each batch
	 * @Deprecated as of 11-29-18, obsolete due to existence BatteryOutlierDrver
	 */
	@Deprecated public static void main(String[] args) throws Exception {
		if(args.length != 2){
			System.out.printf("Usage: BatteryOutliersDriver <input dir> <output dir>\n");
			System.exit(-1);
		}
		
		Configuration conf = new Configuration();
		conf.set("mapreduce.output.textoutputformat.separator", ",");
		Job job = new Job(conf);
		
		job.setJarByClass(BatteryTestCountsDriver.class);
		
		job.setJobName("BatteryOutliersDriver");
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));;
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setMapperClass(BatteryOutliersMapper.class);
		job.setReducerClass(BatteryTestCountReducer.class);
		job.setCombinerClass(BatteryTestCountsCombiner.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}

}