package com.revature;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.revature.mapper.BatteryOutliersMapper;
import com.revature.reducer.BatteryOutlierReducer;
import com.revature.reducer.BatteryTestCountsCombiner;

/**
 * The BatteryOutlierDriver class creates a MapReduce job that identifies outlier battery performance. A battery's performance
 * is considered an outlier when it undergoes a significantly less amount of tests than the batteries in its package. This is done by
 * counting the number of tests taken by each battery within a package.
 * 
 * This MapReduce job is composed of the BatteryOutliersMapper, BatteryTestCountsCombiner, and BatteryOutliersReducer.
 *
 * @author Evan Diamond
 * @version 1.0
 * @since 2018-11-30
 */
public class BatteryOutlierDriver {
	
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
		job.setReducerClass(BatteryOutlierReducer.class);
		job.setCombinerClass(BatteryTestCountsCombiner.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}

}
