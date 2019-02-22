package com.revature.util;

import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

public class ModelFunction{

	public static double[][] execute(Dataset<Row> csv, List<List<Double>> partitions, int splitCount){
		double[][] model = new double[3][4];

		System.out.println("Starting to build model...");
		// Builds verbal score model
		model[0] = logReg(statsDS(binDS(modelDS(csv, "_c1", 1), partitions.get(0),splitCount),splitCount), 1);
		System.out.println("MODEL 1 DONE");
		// Builds exam score model
		model[1] = logReg(statsDS(binDS(modelDS(csv, "_c1", 2), partitions.get(1),splitCount),splitCount), 2);
		System.out.println("MODEL 2 DONE");
		// Builds project score model
		model[2] = logReg(statsDS(binDS(modelDS(csv, "_c1", 3), partitions.get(2),splitCount),splitCount), 3);
		System.out.println("MODEL 3 DONE");
		
		return model;
	}

	private static Dataset<Row> modelDS(Dataset<Row> data, String modelType, int intType) {
		// Filters the data based on test type and finds the mean score and status for an individual associate
		Dataset<Row> modelTypeDS =  data.filter(modelType+" = "+intType).groupBy("_c9").mean("_c3").withColumnRenamed("avg(_c3)", "avg_score");
		Dataset<Row> status = data.filter(modelType+" = "+intType).groupBy("_c9").max("_c10").withColumnRenamed("max(_c10)", "status").withColumnRenamed("_c9", "assc1");

		Dataset<Row> result = modelTypeDS.join(status, modelTypeDS.col("_c9").equalTo(status.col("assc1"))).select("_c9","avg_score","status");
		return result;
	}

	private static Dataset<Row> binDS(Dataset<Row> input, List<Double> partitions, int numBins) {
		partitions.add(100.0); // final value
		Dataset<Row> bins = input.filter("avg_score <= " + partitions.get(0)).withColumn("bin", functions.lit(1));
		int binNum = 2;
		for(int i = 1; i < numBins; i++){
			Dataset<Row> bin;

			// places the mean scores for an associate into bins based on class percentile
			if (Math.abs(partitions.get(i-1) - partitions.get(i)) < 0.0001) {
				double n = 0;
				for (double d:partitions) {
					if (Math.abs(d - partitions.get(i)) < 0.0001) ++n;
				}
				bin = input.filter("abs(avg_score - " + partitions.get(i-1) + ") < 0.0001").
						randomSplit(new double[]{1.0/n,(n-1.0)/n})[0].
						withColumn("bin", functions.lit(binNum));
			} else {
				bin = input.filter("avg_score >= " + partitions.get(i-1) + " and avg_score < "+ partitions.get(i)).
						withColumn("bin", functions.lit(binNum));
			}

			bins = bins.union(bin); // union all
			binNum++;
		}
		return bins;
	}

	private static double[][] statsDS(Dataset<Row> input, int numBins) {
		double prob, logOdds;
		
		// creates a 2-D int array that stores the total count of asscs 
		// and the dropped count of asscs for each bin
		int[][] counts = new int[numBins][];
		
		// creates a 2-D double array that stores:
		// [bin number, probability of being dropped, ln of the odds of being dropped]
		double[][] probs = new double[numBins][3];

		// creates a list of the rows of the dataset to iterate through
		List<Row> inputList = input.collectAsList();

		
		for(int i = 0; i < numBins; i++) {
			counts[i] = new int[]{i, 0, 0};
		}
		
		// instantiates the counts array
		int rowNum = 0;
		for(Row row : inputList) {
			int binNum = row.getInt(3); // gets the bin number
			int status = row.getInt(2); // gets the status for an assc
			counts[binNum-1][1]++; // incremements the total count for the bin 
			if(status == 0) counts[binNum-1][2]++; // checks if an assc is dropped and increments accordingly
			rowNum++; // gets the total number of asscs
		}
				
		// instantiates the probs array
		for(int i = 0; i < numBins; i++) {
			int binTotal = counts[i][1]; // gets the total number of assc in a bin
			int binDropped = counts[i][2]; // gets the total number of dropped asscs in a bin
			
			// checks to make sure there aren't any divisions by zero or ln(0)
			if((binTotal - binDropped) < 1 || binDropped == 0 || binTotal == 0){
				prob = (double) -1; 
				logOdds = (double) -1;
			} else{
				
				// probabilty is calculated by finding the number of asscs dropped 
				// divided by the total number of asscs
				prob = (((double) binDropped)/binTotal*100);  
				
				// calculates the natural log of the odds of being dropped
				// the odds of being dropped are asscs dropped divided by asscs not dropped
				logOdds = Math.log((double) binDropped / (binTotal - binDropped));
			}

			probs[i] = new double[] {i+1, prob, logOdds};
		}
		
		// creates a 2-D double array that stores an assc's:
		// [mean score, ln(odds of that assc being dropped)]
		double[][] stats = new double[rowNum][];
		int i = 0;
		for(Row row : inputList) {
			double score = row.getDouble(1);
			int binNum = row.getInt(3);
			logOdds = probs[binNum-1][2];
			stats[i] = new double[] {score, logOdds};
			i++;
		}
		return stats;
	}

	private static double[] logReg(double[][] stats, int modelNum) {

		// double variables where S indicates sum, x is mean score, y is ln(odds), and 2 indicates squared
		// m indicates the slope for the logistic regression, b is the y-intercept, and r2 is the correlation coefficient
		double Sx, Sy, Sxy, Sx2, Sy2, m, b, r2;
		
		// double array that stores: 
		// [model number, slope, y-intercept, correlation coefficient]
		double[] modelData = new double[4];
		
		// instantiates values to zero
		Sx = Sy = Sxy = Sx2 = Sy2 = 0;
		int n = 0; // takes the totl number of asscs in model

		
		for(int i = 0; i < stats.length; i++) {
			double x = stats[i][0]; // gets the mean score
			double y = stats[i][1]; // gets the ln(odds)

			if(y != -1) { // filters out the error values
				
				// increments the sums and counts
				Sx += x;
				Sy += y;
				Sx2 += x*x;
				Sy2 += y*y;
				Sxy += x*y;
				n++;
			}
		}
		
		m = (n*Sxy - Sx*Sy)/(n*Sx2 - Sx*Sx); // calculates the slope for the regression line
		b = (Sy*Sx2 - Sx*Sxy)/(n*Sx2 - Sx*Sx); // calculates the y-intercept """
		r2 = (n*Sxy - Sx*Sy)*(n*Sxy - Sx*Sy)/((n*Sx2 - Sx*Sx)*(n*Sy2 - Sy*Sy)); //calculates the correlation coefficient """

		// stores the data into the array
		modelData = new double[]{modelNum, m, b, r2};
	
		return modelData;
	}
}