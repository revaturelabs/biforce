package com.revature.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import com.revature.mapper.BatteryAAAMapper;
import com.revature.mapper.BatteryMapper;
import com.revature.reducer.BatteryReducer;

public class InitialCleanseAndSortTest {

  /*
   * Declare harnesses that let you test both mappers and the reducer
   */
  private MapDriver<LongWritable, Text, Text, Text> mapDriver;
  private MapDriver<LongWritable, Text, Text, Text> mapDriverBatteryMapper;
  private ReduceDriver<Text, Text, Text, Text> reduceDriver;

  /*
   * Set up the test. This method will be called before every test.
   */
  @Before
  public void setUp() {

    /*
     * Set up the mapper test harnesses.
     */
    BatteryAAAMapper AAAmapper = new BatteryAAAMapper();
    mapDriver = new MapDriver<LongWritable, Text, Text, Text>();
    mapDriver.setMapper(AAAmapper);
    
    BatteryMapper batteryMapper = new BatteryMapper();
    mapDriverBatteryMapper = new MapDriver<LongWritable, Text, Text, Text>();
    mapDriverBatteryMapper.setMapper(batteryMapper);
    
    /*
     * Set up the reducer test harness.
     */
    BatteryReducer reducer = new BatteryReducer();
    reduceDriver = new ReduceDriver<Text, Text, Text, Text>();
    reduceDriver.setReducer(reducer);
	
  }
  

  /*
   * Test the BatteryAAAmapper.
   */
  
  @Test
  public void testMapperPoor() {
	
    mapDriver.withInput(new LongWritable(1), new Text("372037,2,1,Poor,\"QC_TRAINEE\",2,115509,295048,Employed"));

    mapDriver.withOutput(new Text("295048\t115509\tPassed\tisAAA"), new Text("2\t1"));

    mapDriver.runTest();
  }
  @Test
  public void testMapperGood() {
 
    mapDriver.withInput(new LongWritable(1), new Text("372037,2,1,Good,QC_TRAINEE,2,115509,295048,Employed"));

    mapDriver.withOutput(new Text("295048\t115509\tPassed\tisAAA"), new Text("2\t3"));

    mapDriver.runTest();
  }
  @Test
  public void testMapperAverage() {
 
    mapDriver.withInput(new LongWritable(1), new Text("372037,2,1,Average,QC_TRAINEE,2,115509,295048,Dropped"));

    mapDriver.withOutput(new Text("295048\t115509\tFailed\tisAAA"), new Text("2\t2"));

    mapDriver.runTest();
  }
  @Test
  public void testMapperSuperstar() {
 
    mapDriver.withInput(new LongWritable(1), new Text("372037,2,1,Superstar,QC_TRAINEE,2,115509,295048,Employed"));

    mapDriver.withOutput(new Text("295048\t115509\tPassed\tisAAA"), new Text("2\t4"));

    mapDriver.runTest();
  }
  @Test
  public void testMapperUndefined() {
 
    mapDriver.withInput(new LongWritable(1), new Text("372037,2,1,Undefined,QC_TRAINEE,2,115509,295048,Training"));

    mapDriver.runTest();
  }
  @Test
  public void testMapperNone() {
 
    mapDriver.withInput(new LongWritable(1), new Text("372037,2,1,,QC_TRAINEE,2,115509,295048,Employed"));

    mapDriver.runTest();
  }
  
  String examInputString = "5355,2050,Exam,1,88.3,1,Employed";
  String examOutputKeyString = "5355\t2050\tPassed\tisEEE";
  String examOutputValueString = "1\t88.3";
  
  @Test
  public void testMapperExam() {
 
    mapDriverBatteryMapper.withInput(new LongWritable(1), new Text(examInputString));

    mapDriverBatteryMapper.withOutput(new Text(examOutputKeyString), new Text(examOutputValueString));

    mapDriverBatteryMapper.runTest();
  }
  
  String projectInputString = "5355,2050,Project,1,88.3,1,Employed";
  String projectOutputKeyString = "5355\t2050\tPassed\tisPPP";
  String projectOutputValueString = "1\t88.3";
  
  
  @Test
  public void testMapperProject() {
 
    mapDriverBatteryMapper.withInput(new LongWritable(1), new Text(projectInputString));

    mapDriverBatteryMapper.withOutput(new Text(projectOutputKeyString), new Text(projectOutputValueString));

    mapDriverBatteryMapper.runTest();
  }
  
  String verbalInputString = "5355,2050,Verbal,1,88.3,1,Employed";
  String verbalOutputKeyString = "5355\t2050\tPassed\tisVVV";
  String verbalOutputValueString = "1\t88.3";
  
  
  @Test
  public void testMapperVerbal() {
 
    mapDriverBatteryMapper.withInput(new LongWritable(1), new Text(verbalInputString));

    mapDriverBatteryMapper.withOutput(new Text(verbalOutputKeyString), new Text(verbalOutputValueString));

    mapDriverBatteryMapper.runTest();
  }
  
  String otherInputString = "5355,2050,Other,1,88.3,1,Employed";
  String otherOutputKeyString = "5355\t2050\tPassed\tisOTH";
  String otherOutputValueString = "1\t88.3";
  
  
  @Test
  public void testMapperOther() {
 
    mapDriverBatteryMapper.withInput(new LongWritable(1), new Text(otherInputString));

    mapDriverBatteryMapper.withOutput(new Text(otherOutputKeyString), new Text(otherOutputValueString));

    mapDriverBatteryMapper.runTest();
  }
  
  String projectFirstValueReducerInput = "4\t88.95";
  String projectSecondValueReducerInput = "1\t99.3";
  String projectReducerInputKey="5351\t2050\tPassed\tisPPP";
  String projectReducerOutputKey="5351\t2050\tPassed\tisPPP";
  String projectReducerOutputValue="99.3\t88.95\t\t\t\t\t\t\t\t";

  @Test
  public void testReducerProject() {

    List<Text> values = new ArrayList<Text>();
    values.add(new Text(projectFirstValueReducerInput));
    values.add(new Text(projectSecondValueReducerInput));

    reduceDriver.withInput(new Text(projectReducerInputKey), values);

    reduceDriver.withOutput(new Text(projectReducerOutputKey), new Text(projectReducerOutputValue));

    reduceDriver.runTest();
  }
  
  String projectLargeFirstValueReducerInput = "4\t88.95";
  String projectLargeSecondValueReducerInput = "1\t99.3";
  String projectLargeThirdValueReducerInput = "2\t33.45";
  String projectLargeFourthValueReducerInput = "7\t11.34";
  String projectLargeReducerInputKey="5351\t2050\tPassed\tisPPP";
  String projectLargeReducerOutputKey="5351\t2050\tPassed\tisPPP";
  String projectLargeReducerOutputValue="99.3\t33.45\t88.95\t11.34\t\t\t\t\t\t";

  @Test
  public void testReducerLargeProject() {

    List<Text> values = new ArrayList<Text>();
    values.add(new Text(projectLargeFirstValueReducerInput));
    values.add(new Text(projectLargeSecondValueReducerInput));
    values.add(new Text(projectLargeThirdValueReducerInput));
    values.add(new Text(projectLargeFourthValueReducerInput));

    reduceDriver.withInput(new Text(projectLargeReducerInputKey), values);

    reduceDriver.withOutput(new Text(projectLargeReducerOutputKey), new Text(projectLargeReducerOutputValue));

    reduceDriver.runTest();
  }
  
  String examFirstValueReducerInput = "4\t88.95";
  String examSecondValueReducerInput = "1\t99.3";
  String examReducerInputKey="5351\t2050\tPassed\tisEEE";
  String examReducerOutputKey="5351\t2050\tPassed\tisEEE";
  String examReducerOutputValue="99.3\t\t\t88.95\t\t\t\t\t\t";

  @Test
  public void testReducerExam() {

    List<Text> values = new ArrayList<Text>();
    values.add(new Text(examFirstValueReducerInput));
    values.add(new Text(examSecondValueReducerInput));

    reduceDriver.withInput(new Text(examReducerInputKey), values);

    reduceDriver.withOutput(new Text(examReducerOutputKey), new Text(examReducerOutputValue));

    reduceDriver.runTest();
  }

  
}
