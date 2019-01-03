package com.revature.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

import com.revature.mapper.BatteryOutliersMapper;
import com.revature.reducer.BatteryTestCountsCombiner;
import com.revature.reducer.BatteryTestCountReducer;

public class BatteryTestCountsTest {
	
	private MapDriver<LongWritable, Text, Text, Text>mapDriver;
	private ReduceDriver<Text, Text, Text, Text>combineDriver;
	private ReduceDriver<Text, Text, Text, Text>reduceDriver;
	private MapReduceDriver<LongWritable, Text, Text, Text, Text, Text>mapReduceDriver;
	
	@Before
	public void setUp() {
		BatteryOutliersMapper mapper = new BatteryOutliersMapper();
		mapDriver = new MapDriver<>();
		mapDriver.setMapper(mapper);
		
		BatteryTestCountsCombiner combiner = new BatteryTestCountsCombiner();
		combineDriver = new ReduceDriver<>();
		combineDriver.setReducer(combiner);

		BatteryTestCountReducer reducer = new BatteryTestCountReducer();
		reduceDriver = new ReduceDriver<>();
		reduceDriver.setReducer(reducer);
		
		mapReduceDriver = new MapReduceDriver<>();
		mapReduceDriver.setMapper(mapper);
		mapReduceDriver.setCombiner(combiner);
		mapReduceDriver.setReducer(reducer);
	}
	
	@Test
	public void testMapper() {
		mapDriver.withInput(new LongWritable(1), new Text("280001	110503	Other	isEEE	48.46	71.41	70.27	90	48.06	94.44	92.31		"));
		mapDriver.withOutput(new Text("280001	Other"), new Text("110503	isEEE	48.46	71.41	70.27	90	48.06	94.44	92.31"));
		mapDriver.runTest();
	}
	
	@Test
	public void testCombiner() {
		List<Text> values = new ArrayList<>();
		values.add(new Text("110503	isEEE	48.46	71.41	70.27	90	48.06	94.44	92.31	"));
		values.add(new Text("110503	isAAA	48.46	71.41	70.27	90	48.06	94.44"));
		combineDriver.withInput(new Text("280001	Other"), values);
		combineDriver.withOutput(new Text("110503"), new Text("280001	Other:isEEE	7:isAAA	6"));
		combineDriver.runTest();	
	}
	
	@Test
	public void testReducer() {
		List<Text> values = new ArrayList<>();
		values.add(new Text("280001	Other:isEEE	7:isAAA	6"));
		reduceDriver.withInput(new Text("110503"), values);
		reduceDriver.withOutput(new Text("110503"), new Text("280001	Other	0	7	0	6"));
		reduceDriver.runTest();
	}
	
	@Test
	public void testMapReducer() {
		mapReduceDriver.withInput(new LongWritable(1), new Text("280001	110503	Other	isEEE	48.46	71.41	70.27	90	48.06	94.44	92.31"));
		mapReduceDriver.addOutput(new Text("110503"), new Text("280001	Other	0	7	0	0"));
		mapReduceDriver.runTest();	
	}
	
	@Test
	public void testMapReducerWithManyInputs() {
		mapReduceDriver.withInput(new LongWritable(1), new Text("280001	110503	Other	isEEE	48.46	71.41	70.27	90	48.06	94.44	92.31"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280001	110503	Other	isOTH	48.46	71.41	70.27	90	48.06	94.44	92.31"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280001	110503	Other	isAAA	48.46	71.41	70.27	90	48.06	94.44"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280002	110503	Other	isVVV	48.46	71.41	70.27	90	48.06"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280002	110503	Other	isPPP	48.46	71.41	70.27	90	48.06	94.44	92.31	91.00"));
		mapReduceDriver.addOutput(new Text("110503"), new Text("280001	Other	0	7	0	6,280002	Other	5	0	8	0"));
		mapReduceDriver.runTest();	
	}
}