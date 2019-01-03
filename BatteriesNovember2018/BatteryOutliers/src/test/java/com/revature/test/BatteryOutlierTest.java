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
import com.revature.reducer.BatteryOutlierReducer;
import com.revature.reducer.BatteryTestCountsCombiner;

public class BatteryOutlierTest {
	
	private MapDriver<LongWritable, Text, Text, Text>mapDriver;
	private ReduceDriver<Text, Text, Text, Text>combineDriver;
	private ReduceDriver<Text, Text, Text, Text>reduceDriver;
	private MapReduceDriver<LongWritable, Text, Text, Text, Text, Text>mapReduceDriver;
	
	private String mapperTestString = "280001	110503	Other	isAAA	48.46	71.41	70.27	90	48.06	94.44	92.31		";
	private String mapperTestOutputKey = "280001	Other";
	private String mapperTestOutputValue = "110503	isAAA	48.46	71.41	70.27	90	48.06	94.44	92.31";
	
	private String combinerTestOutputValue = "110503	isEEE	48.46	71.41	70.27	90	48.06	94.44";
	private String combinerTestInputKey = "280001	Other";
	@Before
	public void setUp() {
		BatteryOutliersMapper mapper = new BatteryOutliersMapper();
		mapDriver = new MapDriver<>();
		mapDriver.setMapper(mapper);
		
		BatteryTestCountsCombiner combiner = new BatteryTestCountsCombiner();
		combineDriver = new ReduceDriver<>();
		combineDriver.setReducer(combiner);

		BatteryOutlierReducer reducer = new BatteryOutlierReducer();
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
		mapDriver.withInput(new LongWritable(1), new Text(mapperTestString));
		mapDriver.withOutput(new Text(mapperTestOutputKey), new Text(mapperTestOutputValue));
		mapDriver.runTest();
	}
	
	@Test
	public void testCombiner() {
		List<Text> values = new ArrayList<>();
		values.add(new Text(mapperTestOutputValue));
		values.add(new Text(combinerTestOutputValue));
		combineDriver.withInput(new Text(combinerTestInputKey), values);
		combineDriver.withOutput(new Text("110503"), new Text("280001	Other:isAAA	7:isEEE	6"));
		combineDriver.runTest();	
	}
	
	@Test
	public void testReducer() {
		List<Text> values = new ArrayList<>();
		values.add(new Text("280001	Other:isEEE	7:isAAA	6:isVVV	8:isPPP	3"));
		values.add(new Text("280001	Other:isEEE	7:isAAA	6:isVVV	8:isPPP	3"));
		values.add(new Text("differ	Other:isEEE	7:isAAA	4:isVVV	8:isPPP	3"));
		values.add(new Text("280001	Other:isEEE	7:isAAA	6:isVVV	8:isPPP	3"));
		
		reduceDriver.withInput(new Text("110503"), values);
		reduceDriver.withOutput(new Text("differ	Other	110503"), new Text("4" + "\t" + "6"));
		reduceDriver.runTest();
	}
	
	//Testing single datapoint throughput (returns nothing)
	@Test
	public void testMapReducer() {
		mapReduceDriver.withInput(new LongWritable(1), new Text(mapperTestString));
		mapReduceDriver.runTest();	
	}
	
	@Test
	public void testMapReducerWithManyInputs() {
		mapReduceDriver.withInput(new LongWritable(1), new Text("280001	110503	Other	isEEE	48.46	71.41	70.27	90	48.06	94.44	92.31"));
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
		mapReduceDriver.withInput(new LongWritable(1), new Text("280003	110503	Other	isAAA	48.46	71.41	70.27	90	48.06"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280003	110503	Other	isVVV	48.46	71.41	70.27	90	48.06"));
		mapReduceDriver.withInput(new LongWritable(1), new Text("280003	110503	Other	isPPP	48.46	71.41	70.27	90	48.06	94.44	92.31	91.00"));
		
		mapReduceDriver.addOutput(new Text("280003	Other	110503"), new Text("5	6"));
		mapReduceDriver.runTest();	
	}
}
