package S3ToRedShift.SRS;

//DO NOT UPLOAD!!!!!!!!!!!!!!!!!!

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.ConnectionUtil;

import java.io.*;

public class App 
{
	
	private static String[] s3paths;
	private static String s3key;
	private static String s3sec;
	
	private static FileReader frs3obj;
	private static FileReader frs3key;
	private static FileReader fr3;
	
	
    public static void main( String[] args )
    {	
    	try {
    	frs3key = new FileReader("s3keys.txt");
    	} catch (FileNotFoundException fnfe) {
    		System.out.println("could not find file");
    		System.out.println(fnfe.getMessage());
    		return;
    	}
    	//if (args.length >= 2)
    	{
    		try{
    		char[] c1 = {};
    		frs3key.read(c1);
    		s3key = new String(c1);
    		frs3key.read(c1);
    		s3sec = new String(c1);
    		
    		for(int i = 0; i < args.length; i++){
    			s3paths[i] = args[i];
    		}
    		
    		} catch (IOException ioe) {
    			
    		}
    	} 
    	//else 
    	{
    		//System.out.println("Invalid number of arguments");
    	}
    	
    	//s3paths[0] = "";
    	
    	String[] queries = {
    			"create table battery_test(test_type integer, raw_score integer, score real, test_period integer, test_category integer, builder_id integer, group_id integer, group_type integer, battery_id integer, battery_status integer);",
    			"copy battery_test from '"+s3paths[0]+"' access_key_id '"+s3key+"' secret_access_key '"+s3sec+"' format as csv;",
    			//"create table if not exists ml_output(battery_id integer not null, pass_fail decimal not null);",
    			//"copy ml_output from '"+s3path+"' access_key_id '"+s3key+"' secret_access_key '"+s3sec+"' format as csv;",
    			"create table if not exists spark_output(battery_id integer not null, fail_chance real not null, sample_size integer);",
    			"copy ml_output from '"+s3paths[1]+"' access_key_id '"+s3key+"' secret_access_key '"+s3sec+"' format as csv;",
    			"create table battery_status_legend(battery_status integer, name varchar);",
    			"copy battery_status_legend from '"+s3paths[2]+"' access_key_id '"+s3key+"' secret_access_key '"+s3sec+"' format as csv;"
    	};
    	
    	Connection con = null;
    	
    	try {
			con = ConnectionUtil.getConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	
    	if (con == null) {
			return;
		}
    	
    	try {
    	PreparedStatement ps;
    	
    	for (String s : queries){
    		ps = con.prepareStatement(s);
    		ps.executeQuery();
    	}
    	
    	//ps = con.prepareStatement("");
    	//ps.executeQuery();
    	
    	} catch (SQLException se){
    		
    	}
    	
    }
}
