package com.revature;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.revature.mapper.TestMapper;

/**
 * driver to test mapper's ability to receive input 
 *
 * @author Evan Diamond
 * @deprecated
 */
public class LengthTestDriver {
	/***
	 * Used to test that input of Mapper can be properly split
	 * 
	 * @deprecated as of 11-19-18
	 * @param args
	 * @throws Exception
	 */
	@Deprecated public static void main(String[] args) throws Exception {
		if(args.length != 2){
			System.out.printf("Usage: BatteryDriver <Battery Performance input dir> <Battery Note input dir> <output dir>\n");
			System.exit(-1);
		}
		
		Job job = new Job();
		
		job.setJarByClass(LengthTestDriver.class);
		
		job.setJobName("LengthTestDriverDriver");
		
		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, TestMapper.class);
		
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setNumReduceTasks(0);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}

}
