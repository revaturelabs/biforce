package com.revature.mapper;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.revature.util.Masker;

/**
 * The BatteryAAAMapper class is responsible for writing to the intermediate output a desired Key-Value pair. Additionally,
 * the mapper masks ratings for records that contain "isAAA" as its test_type.
 *
 * @author Bryn Portella & Scott Marshall
 * @version 1.0
 * @since 2018-11-30
 */
public class BatteryAAAMapper extends Mapper<LongWritable, Text, Text, Text>{
	
	/***
	 * The map method takes in single lines from a CSV file and parses each line using its comma delimiter. The delimited values are stored
	 * in an array of String and manipulated to match the desired Key-Value output. The form of this output is as follows:
	 *
	 * Key, as a single tab-delimited Text: battery_id, battery_group, battery_status, test_type
	 * Value, as a single tab-delimited Text: test_period, AAAFeedbackNumeric
	 * Note: AAAFeedbackNumeric is a masked rating for records that contain "isAAA" as its test_type; "Poor" is 1, "Average" is 2, "Good" is 3, and "Superstar" is 4.
	 * 
	 * @param LongWritable key This receives the unique key associated with each record, where a record is a single line.
	 * @param value This receives a record, where a record is a single line.
	 * @param context This receives the environment for running the operations.
	 * @return void 
	 * @exception throws IOException on input error.
	 * @exception throws InterruptedException on thread interruption.
	 * @see IOException
	 * @see InterruptedException
	 */
	@Override
	  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

	    String[] AAARecord = value.toString().split(",");
	    if (AAARecord.length<9) return;
	    
	    String testType = "isAAA";
	    int batteryGroupIndex = 6;
	    int batteryIdIndex = 7;
	    int AAAStatusIndex= 3;
	    int testPeriodIndex = 5;
	    int batteryStatusIndex = 8;
	    String batteryStatus = Masker.mask(AAARecord[batteryStatusIndex]);
	    String AAAFeedback = AAARecord[AAAStatusIndex];
	    
	    String outputKey = "";
	    String outputValue = "";
	    
	    String delimiter = "\t";
	    
	    outputKey = AAARecord[batteryIdIndex]+delimiter + AAARecord[batteryGroupIndex] + delimiter + batteryStatus;
	    
	    outputKey += delimiter+testType;
	    
	    int AAAFeedbackNumeric;
	    
	    switch(AAAFeedback){
	    	case "Poor": AAAFeedbackNumeric = 1;
	    		break;
	    	case "Average":AAAFeedbackNumeric = 2;
	    		break;
	    	case "Good": AAAFeedbackNumeric =3;
	    		break;
	    	case "Superstar": AAAFeedbackNumeric = 4;
	    		break;
	    	default: return;
	    }
	    
	    
	    outputValue = AAARecord[testPeriodIndex]+delimiter+AAAFeedbackNumeric;
	    
	    context.write(new Text(outputKey), new Text(outputValue));
	    
	    
	  }
}
