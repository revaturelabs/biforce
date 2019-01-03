package com.revature;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.revature.mapper.BatteryOutliersMapper;
import com.revature.reducer.BatteryMedianCombiner;
import com.revature.reducer.BatteryMedianReducer;

/**
 * The BatteryMedianDriver class creates a MapReduce job that identifies outlier battery performance. A battery's performance
 * is considered an outlier when the median of its tests scores is significantly less than the median of each battery's tests score's performance median.
 *
 * This MapReduce job is composed of the BatteryOutlierMapper, BatteryMedianCombiner, and BatteryMedianReducer.
 *
 * @author Evan Diamond
 * @version 1.0
 * @since 2018-11-30
 */
public class BatteryMedianDriver {
	
	/***
	 * This is the main method which creates the MapReduce job for the aforementioned tasks.
	 * 
	 * @param args[0] input directory
	 * @param args[1] output directory
	 * @return void 
	 * @exception throws IOException on input error.
	 * @exception throws InterruptedException on thread interruption.
	 * @see IOException
	 * @see InterruptedException
	 */
	public static void main(String[] args) throws Exception {
		if(args.length != 2){
			System.out.printf("Usage: BatteryOutliersDriver <input dir> <output dir>\n");
			System.exit(-1);
		}
		
		Job job = new Job();
		
		job.setJarByClass(BatteryTestCountsDriver.class);
		
		job.setJobName("BatteryOutliersDriver");
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));;
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setMapperClass(BatteryOutliersMapper.class);
		job.setReducerClass(BatteryMedianReducer.class);
		job.setCombinerClass(BatteryMedianCombiner.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}

}
