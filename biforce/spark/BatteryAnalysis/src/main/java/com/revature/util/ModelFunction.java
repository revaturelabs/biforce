package com.revature.util;

import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

/**
 * Create models for different test types
 * @author Mason Wegert
 * @author Diego Gomez
 *
 */
public class ModelFunction{
	/**
	 * The main driver method. Calls the other methods and returns tuning parameters for each model.
	 * The fourth test type is excluded because it has a low correlation and negatively effects the results.
	 * @param csv - input csv
	 * @param partitions - output of partitionFinder, list of lists with a percentile for the splitCount
	 * @param splitCount - must match number of splits/buckets
	 * @return model - list, 0 is model number, 1 is slope for the regression line, 2 is y-intercept, 3 is regression r2 correlation value
	 */
	public static double[][] execute(Dataset<Row> csv, List<List<Double>> partitions, int splitCount){
		double[][] model = new double[3][4];

		System.out.println("Starting to build model...");
		// Builds verbal score model
		model[0] = logReg(statsDS(binDS(modelDS(csv, 1), partitions.get(0),splitCount),splitCount), 1);
		System.out.println("MODEL 1 DONE");
		// Builds exam score model
		model[1] = logReg(statsDS(binDS(modelDS(csv, 2), partitions.get(1),splitCount),splitCount), 2);
		System.out.println("MODEL 2 DONE");
		// Builds project score model
		model[2] = logReg(statsDS(binDS(modelDS(csv, 3), partitions.get(2),splitCount),splitCount), 3);
		System.out.println("MODEL 3 DONE");
		
		return model;
	}
	
	/**
	 * Filters test type, groups by associate id, calculates the average score for that test type,
	 * and calculates the max (we aggregate to find a single value, they should all be the same though) status.
	 * @param data - input csv
	 * @param testType - which test type
	 * @return result - csv with only associate id, average score, and status columns
	 */
	private static Dataset<Row> modelDS(Dataset<Row> data, int testType) {
		// Filters the data based on test type and finds the mean score and status for an individual associate
		Dataset<Row> modelTypeDS =  data.filter("_c1 = "+testType).groupBy("_c9").mean("_c3").withColumnRenamed("avg(_c3)", "avg_score");
		Dataset<Row> status = data.filter("_c1 = "+testType).groupBy("_c9").max("_c10").withColumnRenamed("max(_c10)", "status").withColumnRenamed("_c9", "assc1");

		Dataset<Row> result = modelTypeDS.join(status, modelTypeDS.col("_c9").equalTo(status.col("assc1"))).select("_c9","avg_score","status");
		return result;
	}

	/**
	 * 
	 * @param input - input sql table like data with only associate id, avg score, and status
	 * @param partitions - the partition values for the different buckets
	 * @param numBins - number buckets
	 * @return bins - data filtered to be in all the different buckets, with "bin" num column added
	 */
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

	/**
	 * converts the output of binDS (a sql table with ass id, avg score, status, and binnum) into
	 * a list of values for each input row stating a score and logodds.
	 * @param input - sql table like data with associate id, avg score, status, and binNum columns
	 * @param numBins - number of bins/buckets
	 * @return stats - list of lists with a list of values for each of the input rows (one row per
	 * associate and test type) with a score and logodds for each
	 */
	private static double[][] statsDS(Dataset<Row> input, int numBins) {
		double prob, logOdds;
		
		// Creates a 2-D int array that stores the total count of asscs 
		// and the dropped count of asscs for each bin
		int[][] counts = new int[numBins][];
		
		// Creates a 2-D double array that stores:
		// [bin number, probability of being dropped, ln of the odds of being dropped]
		double[][] probs = new double[numBins][3];

		// creates a list of the rows of the dataset to iterate through
		List<Row> inputList = input.collectAsList();

		
		for(int i = 0; i < numBins; i++) {
			counts[i] = new int[]{i, 0, 0};
		}
		
		// Instantiates the counts array
		int rowNum = 0;
		for(Row row : inputList) {
			int binNum = row.getInt(3); // gets the bin number
			int status = row.getInt(2); // gets the status for an assc
			counts[binNum-1][1]++; // increments the total count for the bin 
			if(status == 0) counts[binNum-1][2]++; // checks if an assc is dropped and increments accordingly
			rowNum++; // gets the total number of asscs
		}

		// Instantiates the probs array

		for(int i = 0; i < numBins; i++) {
			int binTotal = counts[i][1]; // Gets the total number of assc in a bin
			int binDropped = counts[i][2]; // Gets the total number of dropped asscs in a bin
			
			// Checks to make sure there aren't any divisions by zero or ln(0)
			if((binTotal - binDropped) < 1 || binDropped == 0 || binTotal == 0){
				prob = (double) -1; 
				logOdds = (double) -1;
			} else{
				
				// Probabilty is calculated by finding the number of asscs dropped 
				// divided by the total number of asscs
				prob = (((double) binDropped)/binTotal*100);  
				
				// Calculates the natural log of the odds of being dropped
				// the odds of being dropped are asscs dropped divided by asscs not dropped
				logOdds = Math.log((double) binDropped / (binTotal - binDropped));
			}

			probs[i] = new double[] {i+1, prob, logOdds};
		}
		
		// Creates a 2-D double array that stores an assc's:
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

	/**
	 * Takes a list of values for each associate id/test type and returns a description of the logistic 
	 * regression line parameters.
	 * @param stats - multiarray, 0th level one value per associate id/test type pair, 1st level score and logodds
	 * @param modelNum - number of the test type
	 * @return modelData - array, 0 modelNum, 1 slope of regression line, 2 y-intercept, 3 correlation for r2
	 */
	private static double[] logReg(double[][] stats, int modelNum) {

		// Double variables where S indicates sum, x is mean score, y is ln(odds), and 2 indicates squared
		// m indicates the slope for the logistic regression, b is the y-intercept, and r2 is the correlation coefficient
		double Sx, Sy, Sxy, Sx2, Sy2, m, b, r2;
		
		// Double array that stores: 
		// [model number, slope, y-intercept, correlation coefficient]
		double[] modelData = new double[4];
		
		// Instantiates values to zero
		Sx = Sy = Sxy = Sx2 = Sy2 = 0;
		int n = 0; // takes the totl number of asscs in model

		
		for(int i = 0; i < stats.length; i++) {
			double x = stats[i][0]; // gets the mean score
			double y = stats[i][1]; // gets the ln(odds)


			if(y != -1) { // filters out the error values
				
				// Increments the sums and counts
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

		// Stores the data into the array
		modelData = new double[]{modelNum, m, b, r2};
	
		return modelData;
	}
}