package com.revature;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.revature.mapper.BatteryAAAMapper;
import com.revature.mapper.BatteryMapper;
import com.revature.reducer.BatteryReducer;

/**
 * The BatteryDriver class creates a MapReduce job to further denormalize the data and better format it for further MapReduce processing.
 *
 * @author Evan Diamond, Daniel Campos-Bravo, & Jesse Hummel
 * @version 1.0
 * @since 2018-11-30
 */
public class BatteryDriver {
	/***
	 * This is the main method which creates the MapReduce job for the aforementioned tasks.
	 * 
	 * @param args[0] Battery Performance input directory
	 * @param args[1] Battery Note input directory
 	 * @param args[2] output directory
	 * @return void 
	 * @exception throws IOException on input error.
	 * @exception throws InterruptedException on thread interruption.
	 * @see IOException
	 * @see InterruptedException
	 */
	public static void main(String[] args) throws Exception {
		if(args.length != 3){
			System.out.printf("Usage: BatteryDriver <Battery Performance input dir> <Battery Note input dir> <output dir>\n");
			System.exit(-1);
		}
		
		Job job = new Job();
		
		job.setJarByClass(BatteryDriver.class);
		
		job.setJobName("BatteryDriver");
		
		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, BatteryMapper.class);
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, BatteryAAAMapper.class);
		
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		
		job.setReducerClass(BatteryReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}

}