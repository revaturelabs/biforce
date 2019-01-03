package com.revature.mapper;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

/**
 * test mapper that outputs input split into string arrua
 * @Deprecated as of 11-29-18,
 * @author Evan Diamond
 *
 */
public class TestMapper extends Mapper<LongWritable, Text, Text, Text>{
	/**
	 * @deprecated as of 11-29-18
	 * Used to test if input is being properly split into an array
	 */
	 @Override
	 @Deprecated public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		 String[] record = value.toString().split(",");
		 context.write(new Text(new Integer(record.length).toString()), new Text(Arrays.toString(record)));
	 }

}
