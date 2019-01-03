package com.revature.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import com.revature.mapper.BatteryOutliersMapper;
import com.revature.reducer.BatteryMedianCombiner;
import com.revature.reducer.BatteryMedianReducer;
import com.revature.reducer.BatteryOutlierReducer;

public class BatteryMedianTest {
	
	private MapDriver<LongWritable, Text, Text, Text>mapDriver;
	private ReduceDriver<Text, Text, Text, Text>combineDriver;
	private ReduceDriver<Text, Text, Text, Text>reduceDriver;
	private MapReduceDriver<LongWritable, Text, Text, Text, Text, Text>mapReduceDriver;
	
	@Before
	public void setUp() {
		BatteryOutliersMapper mapper = new BatteryOutliersMapper();
		mapDriver = new MapDriver<>();
		mapDriver.setMapper(mapper);
		
		BatteryMedianCombiner combiner = new BatteryMedianCombiner();
		combineDriver = new ReduceDriver<>();
		combineDriver.setReducer(combiner);

		BatteryMedianReducer reducer = new BatteryMedianReducer();
		reduceDriver = new ReduceDriver<>();
		reduceDriver.setReducer(reducer);
		
		mapReduceDriver = new MapReduceDriver<>();
		mapReduceDriver.setMapper(mapper);
		mapReduceDriver.setCombiner(combiner);
		mapReduceDriver.setReducer(reducer);
	}
	
	@Test
	public void testMode(){
		Integer[] arr = {1,2,3,3,4, 3, 3, 6, 6, 7, 1, 99, 23, 4};
		List<Integer> list = Arrays.asList(arr);
		assertEquals(3, BatteryOutlierReducer.mode(list));
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
		values.add(new Text("110503	isEEE	48.46	71.41	70.27	90	48.06	94.44	92.31"));
		values.add(new Text("110503	isAAA	48.46	71.41	70.27	90	48.06	94.44"));
		combineDriver.withInput(new Text("280001	Other"), values);
		combineDriver.withOutput(new Text("110503"), new Text("280001	Other:isEEE	71.41:isAAA	70.84"));
		combineDriver.runTest();	
	}
	
	@Test
	public void testCombinerFilter() {
		List<Text> values = new ArrayList<>();
		values.add(new Text("110503	isAAA	48.44"));
		values.add(new Text("110503	isVVV	48.44"));
		combineDriver.withInput(new Text("280001	Other"), values);
		combineDriver.runTest();	
	}
	
	@Test
	public void testCombinerPartialFilter() {
		List<Text> values = new ArrayList<>();
		values.add(new Text("110503	isAAA	48.44				"));
		values.add(new Text("110503	isEEE	48.46	71.41	70.27	90	48.06	94.44	92.31"));
		combineDriver.withInput(new Text("280001	Passed"), values);
		combineDriver.withOutput(new Text("110503"), new Text("280001	Passed:isAAA	48.44:isEEE	71.41"));
		combineDriver.runTest();	
	}
	
	@Test
	public void testReducer() {
		List<Text> values = new ArrayList<>();
		values.add(new Text("280001	Other:isEEE	74.41:isAAA	2:isVVV	80.0"));
		values.add(new Text("280002	Other:isEEE	73.41:isAAA	2:isVVV	80.0:isPPP	66.1"));
		values.add(new Text("280003	Other:isEEE	72.41:isAAA	2:isVVV	80.0:isPPP	46.1"));
		values.add(new Text("280004	Other:isEEE	71.41:isAAA	2:isVVV	60.0:isPPP	66.1"));
		values.add(new Text("280005	Other:isEEE	70.41:isAAA	1:isVVV	80.0:isPPP	66.1"));
		
		reduceDriver.withInput(new Text("110503"), values);
		reduceDriver.withOutput(new Text("280005	Other	110503"), new Text("isEEE: 70.41 vs 72.41;	isAAA: 1.0 vs 2.0;	"));
		reduceDriver.withOutput(new Text("280003	Other	110503"), new Text("isPPP: 46.1 vs 66.1;	"));
		reduceDriver.withOutput(new Text("280004	Other	110503"), new Text("isVVV: 60.0 vs 80.0;	"));
		reduceDriver.runTest();
	}
	
	@Test
	public void testMapReducerWithManyInputs() {
		mapReduceDriver.withInput(new LongWritable(1), new Text("280001	110503	Other	isEEE	8.46	1.41	0.27	9	8.06	4.44	2.31"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280001	110503	Other	isOTH	48.46	71.41	70.27	90	48.06	94.44	92.31"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280001	110503	Other	isAAA	48.46	71.41	70.27	90	48.06	94.44"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280001	110503	Other	isVVV	48.46	71.41	70.27	90	48.06"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280001	110503	Other	isPPP	48.46	71.41	70.27	90	48.06	94.44	92.31	91.00"));
		
		mapReduceDriver.withInput(new LongWritable(1), new Text("280002	110503	Other	isEEE	48.46	71.41	70.27	90	48.06	94.44	92.31"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280002	110503	Other	isOTH	48.46	71.41	70.27	90	48.06	94.44	92.31"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280002	110503	Other	isAAA	48.46	71.41	70.27	90	48.06	94.44"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280002	110503	Other	isVVV	48.46	71.41	70.27	90	48.06"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280002	110503	Other	isPPP	48.46	71.41	70.27	90	48.06	94.44	92.31	91.00"));
		
		mapReduceDriver.withInput(new LongWritable(1), new Text("280003	110503	Other	isEEE	48.46	71.41	70.27	90	48.06	94.44	92.31"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280003	110503	Other	isOTH	48.46	71.41	70.27	90	48.06	94.44	92.31"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280003	110503	Other	isAAA	48.46	71.41	70.27	90	48.06	94.44"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280003	110503	Other	isVVV	48.46	71.41	70.27	90	48.06"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280003	110503	Other	isPPP	48.46	71.41	70.27	90	48.06	94.44	92.31	91.00"));
		
		mapReduceDriver.addOutput(new Text("280001	Other	110503"), new Text("isEEE: 4.44 vs 71.41;	"));
		mapReduceDriver.runTest();	
	}
}
