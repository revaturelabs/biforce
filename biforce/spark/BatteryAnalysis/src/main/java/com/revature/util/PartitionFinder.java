package com.revature.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row; 

//#1,2,25,74.00,7,20,14,112511,2,281214,2
//#_c0 = 1 : row number
//#_c1 = 2 : A.ASSESSMENT_TYPE
//# - 'VERBAL' = 1; 'EXAM' = 2;
//# - 'PROJECT' = 3; 'OTHER' = 4
//#_c2 = 25 : A.RAW_SCORE
//#_c3 = 74.00 : Q.SCORE
//#_c4 = 7 : A.WEEK_NUMBER 1-9 inclusive
//#_c5 = 20 : A.ASSESSMENT_CATEGORY
//#_c6 = 14 : G.TRAINER_ID
//#_c7 = 112511 : G.BATCH_ID
//#_c8 = 2 : G.SKILL_TYPE 
//# - 'SDET' = 1; 'J2EE' = 2; 'OTHER' = 3;
//# - 'BPM' = 4; 'NET' = 5; 'MICROSERVICES' = 6
//#_c9 = 281214 : Q.TRAINEE_ID
//#_c10 = 2 : B.TRAINING_STATUS 
//# - 'DROPPED'= 0; 'EMPLOYED' = 1; 'TRAINING' = 2; 
//# - 'SIGNED' = 3; 'CONFIRMED' = 4; 'MARKETING' = 5
public class PartitionFinder {
	public static List<List<Double>> read(Dataset<Row> csv) {
		Dataset<Row> csv2 = csv.drop("_c0","_c2","_c5","_c6","_c7","_c8"); //1,3,4,9,10
		Dataset<Row> definitiveTrainees = csv2.filter("_c10 = 0 OR _c10 = 1 OR _c4 = 8 OR _c4 = 9").dropDuplicates();
		Dataset<Row> valid1 = csv2.join(definitiveTrainees,csv2.col("_c9").equalTo(definitiveTrainees.col("_c9")),"left_semi").filter("_c4<=3");
		Dataset<Row> sufficientDataTrainees = csv2.filter("_c4>2").dropDuplicates();
		Dataset<Row> valid2 = valid1.join(sufficientDataTrainees, valid1.col("_c9").equalTo(sufficientDataTrainees.col("_c9")),"left_semi");
		
		Dataset<Row> week1 = valid2.filter("_c4 = 1").drop("_c1","_c4");
		Dataset<Row> avgWeek1 = week1.groupBy("_c9").avg("_c3").drop("_c9");
		Dataset<Row> descWeek1= avgWeek1.describe("avg(_c3)");
		String meanWeek1Str = descWeek1.where("summary = 'mean'").collectAsList().get(0).getString(1);
		String stddevWeek1Str = descWeek1.where("summary = 'stddev'").collectAsList().get(0).getString(1);
		Double meanWeek1 = Double.valueOf(meanWeek1Str);
		Double stddevWeek1 = Double.valueOf(stddevWeek1Str);
		List<Double> week1centiles = getPercentiles(meanWeek1, stddevWeek1);
		System.out.println(week1centiles);
		
		Dataset<Row> week2 = valid2.filter("_c4 = 2").drop("_c1","_c4");
		Dataset<Row> avgWeek2 = week2.groupBy("_c9").avg("_c3").drop("_c9");
		Dataset<Row> descWeek2= avgWeek2.describe("avg(_c3)");
		String meanWeek2Str = descWeek2.where("summary = 'mean'").collectAsList().get(0).getString(1);
		String stddevWeek2Str = descWeek2.where("summary = 'stddev'").collectAsList().get(0).getString(1);
		Double meanWeek2 = Double.valueOf(meanWeek2Str);
		Double stddevWeek2 = Double.valueOf(stddevWeek2Str);
		List<Double> week2centiles = getPercentiles(meanWeek2, stddevWeek2);
		Dataset<Row> week3 = valid2.filter("_c4 = 3").drop("_c1","_c4");
		Dataset<Row> avgWeek3 = week3.groupBy("_c9").avg("_c3").drop("_c9");
		Dataset<Row> descWeek3= avgWeek3.describe("avg(_c3)");
		String meanWeek3Str = descWeek3.where("summary = 'mean'").collectAsList().get(0).getString(1);
		String stddevWeek3Str = descWeek3.where("summary = 'stddev'").collectAsList().get(0).getString(1);
		Double meanWeek3 = Double.valueOf(meanWeek3Str);
		Double stddevWeek3 = Double.valueOf(stddevWeek3Str);
		List<Double> week3centiles = getPercentiles(meanWeek3, stddevWeek3);
		Dataset<Row> test1 = valid2.filter("_c1 = 1").drop("_c1","_c4");
		Dataset<Row> avgTest1 = test1.groupBy("_c9").avg("_c3").drop("_c9");
		Dataset<Row> descTest1= avgTest1.describe("avg(_c3)");
		String meanTest1Str = descTest1.where("summary = 'mean'").collectAsList().get(0).getString(1);
		String stddevTest1Str = descTest1.where("summary = 'stddev'").collectAsList().get(0).getString(1);
		Double meanTest1 = Double.valueOf(meanTest1Str);
		Double stddevTest1 = Double.valueOf(stddevTest1Str);
		List<Double> test1centiles = getPercentiles(meanTest1, stddevTest1);
		Dataset<Row> test2 = valid2.filter("_c1 = 2").drop("_c1","_c4");
		Dataset<Row> avgTest2 = test2.groupBy("_c9").avg("_c3").drop("_c9");
		Dataset<Row> descTest2= avgTest2.describe("avg(_c3)");
		String meanTest2Str = descTest2.where("summary = 'mean'").collectAsList().get(0).getString(1);
		String stddevTest2Str = descTest2.where("summary = 'stddev'").collectAsList().get(0).getString(1);
		Double meanTest2 = Double.valueOf(meanTest2Str);
		Double stddevTest2 = Double.valueOf(stddevTest2Str);
		List<Double> test2centiles = getPercentiles(meanTest2, stddevTest2);
		Dataset<Row> test3 = valid2.filter("_c1 = 3").drop("_c1","_c4");
		Dataset<Row> avgTest3 = test3.groupBy("_c9").avg("_c3").drop("_c9");
		Dataset<Row> descTest3= avgTest3.describe("avg(_c3)");
		String meanTest3Str = descTest3.where("summary = 'mean'").collectAsList().get(0).getString(1);
		String stddevTest3Str = descTest3.where("summary = 'stddev'").collectAsList().get(0).getString(1);
		Double meanTest3 = Double.valueOf(meanTest3Str);
		Double stddevTest3 = Double.valueOf(stddevTest3Str);
		List<Double> test3centiles = getPercentiles(meanTest3, stddevTest3);
		Dataset<Row> test4 = valid2.filter("_c1 = 4").drop("_c1","_c4");
		Dataset<Row> avgTest4 = test4.groupBy("_c9").avg("_c3").drop("_c9");
		Dataset<Row> descTest4= avgTest4.describe("avg(_c3)");
		String meanTest4Str = descTest4.where("summary = 'mean'").collectAsList().get(0).getString(1);
		String stddevTest4Str = descTest4.where("summary = 'stddev'").collectAsList().get(0).getString(1);
		Double meanTest4 = Double.valueOf(meanTest4Str);
		Double stddevTest4 = Double.valueOf(stddevTest4Str);
		List<Double> test4centiles = getPercentiles(meanTest4, stddevTest4);
		List<List<Double>> output = new ArrayList<>();
		output.add(week1centiles);
		output.add(week2centiles);
		output.add(week3centiles);
		output.add(test1centiles);
		output.add(test2centiles);
		output.add(test3centiles);
		output.add(test4centiles);
		return output;
	}
	private static List<Double> getPercentiles(Double mean,Double stddev) {
		//z=X-mu/sigma
		//X=z*sigma+mu
		List<Double> percentiles = new ArrayList<>();
		Double[] zScores = {-1.2816,-0.8416,-0.5244,-0.2533,0.0,0.2533,0.5244,0.8416,1.2816}; 
		for (Double d: zScores) {
			percentiles.add(d*stddev+mean);
		}
 		return percentiles;
	}
}
