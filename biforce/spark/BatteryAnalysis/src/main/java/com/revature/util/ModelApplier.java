package com.revature.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import scala.Tuple2;

/**
 * Contains methods to apply the models to Datasets, evaluate accuracy, and to
 * calculate optimal % fail chance to split drop/pass.
 * 
 * @see ModelFunction
 * @author Mason Wegert
 * @author Diego Gomez
 */

//TODO javadoc
public class ModelApplier {

	/**
	 * Takes the input csv and applies the model to calculate partial fail percents for that test
	 * returns a JavaRDD<Row> in new format with failpercent and r^2 added, unused columns truncated.
	 * 
	 * @param csv input csv (should be model set)
	 * @param coefs coefficients array, output of ModelFunction.execute
	 * @return csvRDD - a JavaRDD<Row> object with input tests processed with input coef to calculate fail chance
	 *                  row output columns are: id, calculated fail %, r^2, status, test period
	 */
	private static JavaRDD<Row> findSums(Dataset<Row> csv, double[][] coefs) {
		JavaRDD<Row> csvRDD = csv.javaRDD()// .filter(row -> row.getInt(1) != 4) // get rid of test 4, it's too
											// inaccurate
				.map(row -> {
					double failPercent = 0;
					double rsq = 0;

					for (int i = 1; i < 4; ++i) {
						if (Double.isNaN(coefs[i - 1][1]) || Double.isInfinite(coefs[i - 1][1])) {
							return RowFactory.create(row.getInt(9), 0.0, 0.0, 0);
						}

						if (row.getInt(1) == i) { // Assessment type 1-3
							rsq = coefs[i - 1][3];
							failPercent = Math.exp(row.getDouble(3) * coefs[i - 1][1] + coefs[i - 1][2])
									/ (1 + Math.exp(row.getDouble(3) * coefs[i - 1][1] + coefs[i - 1][2]))
									* coefs[i - 1][3];
							// multiplies partial fail % by the r^2 to weight. Division by total r^2 will
							// happen later.
							break;
						}
					}
					int status = row.getInt(10) == 0 ? 0 : 1;

					// row: (int, int, int, double, int, int, int, int, int, int (id), int)
					Row outputRow = RowFactory.create(row.getInt(9), failPercent, rsq, status, row.getInt(4));
					return outputRow;
				});
		return csvRDD;
	}

	/**
	 * Applies the given model to the dataset to predict fail % and returns JavaPairRDD with
	 * battery id as the Integer key and the partial fail chances summed. 
	 * 
	 * @param csv input csv, should be whole data set
	 * @param coefs coefficients array, output of ModelFunction.execute
	 * @return output - JavaPairRDD<Integer, Row> where key is battery ID
	 *                  row output columns are: summed fail chance, summed r^2, status, highest week of testing.
	 */
	public static JavaPairRDD<Integer, Row> applyModel(Dataset<Row> csv, double[][] coefs) {
		JavaRDD<Row> csvRDD = findSums(csv, coefs);

		// map to id | row as a pair. Sum up all the weighted partial drop percents
		// as well as the sum of the r^2's for normalization.
		JavaPairRDD<Integer, Row> applicationRDD = csvRDD
				.mapToPair(row -> new Tuple2<Integer, Row>(row.getInt(0), row));
		JavaPairRDD<Integer, Row> sums = applicationRDD.reduceByKey((Row row1, Row row2) -> {
			return RowFactory.create(row1.getInt(0), row1.getDouble(1) + row2.getDouble(1),
					row1.getDouble(2) + row2.getDouble(2), row1.getInt(3),
					row1.getInt(4) > row2.getInt(4) ? row1.getInt(4) : row2.getInt(4));
		});
		return sums;
	}

	/**
	 * Applies the given model to the dataset to predict fail % and returns JavaRDD with output.
	 * Limits model to first "maxWeek" test
	 * 
	 * @param csv input csv, should be model set
	 * @param coefs coefficients array, output of ModelFunction.execute
	 * @param maxWeek maximum week to test
	 * @return output - JavaRDD<Row>
	 *                  row output columns are: ID, normalized fail chance, status of battery (0 or 1)
	 */
	public static JavaRDD<Row> applyControlModel(Dataset<Row> csv, double[][] coefs, int maxWeek) {
		// Only include associates who are past a certain week in training
		// The following code block actually reduces the accuracy. Test with a larger
		// dataset.
		/*
		 * Dataset<Row> currentWeek = csv.groupBy("_c9").max("_c4").where("max(_c4) >= "
		 * + maxWeek).withColumnRenamed("_c9", "id"); csv = csv.join(currentWeek,
		 * col("_c9").equalTo(col("id")), "leftsemi");
		 */

		// limit to a certain number of available weeks
		JavaRDD<Row> csvRDD = findSums(csv.filter("_c4 <=" + maxWeek).filter("_c1 != 4"), coefs);

		// map to id | row as a pair. Sum up all the weighted partial drop percents as
		// well as the sum of the r^2's for normalization.
		JavaPairRDD<Integer, Row> applicationRDD = csvRDD
				.mapToPair(row -> new Tuple2<Integer, Row>(row.getInt(0), row));
		JavaPairRDD<Integer, Row> sums = applicationRDD.reduceByKey((Row row1, Row row2) -> {
			return RowFactory.create(row1.getInt(0), row1.getDouble(1) + row2.getDouble(1),
					row1.getDouble(2) + row2.getDouble(2), row1.getInt(3));
		});

		return sums.map(pairTuple -> {
			// Associate ID | % failure | fail (1 or 0)
			return RowFactory.create(pairTuple._1, pairTuple._2.getDouble(1) / pairTuple._2.getDouble(2),
					pairTuple._2.getInt(3));
		});
	}

	/**
	 * Find the optimal cutoff point (by drop chance %) on where our probability
	 * predictor should be divided into pass/fail by iterating through percentages
	 * 0%-100% and calculating the accuracy (Highest accuracy percentage is
	 * "optimal")
	 * 
	 * @param controlRDD The RDD to evaluate on. c1 = battery id, c2 =
	 *                      drop/pass (0/1), c3 = fail % in decimal
	 * @param accuracyDelta Percentage point to iterate through by in decimals.
	 *                      Ex: 0 = 0% 1 = 100%.
	 * @return OptimalPoint object holding the optimal cutoff point in %, the number
	 *         of correct guesses (true positive/true negatives / population), and
	 *         the total population
	 */
	public static OptimalPoint findOptimalPercent(JavaRDD<Row> controlRDD, double accuracyDelta) {
		List<Double> percentList = new ArrayList<>();
		long optimalAccurateCount = 0;
		double optimalPercent = 0;

		// Simply iterate through percentages and find the cutoff point with the highest
		// # of correct predictions
		for (Double d = 0.0; d <= 1; d += accuracyDelta) {
			percentList.add(d);
		}

		for (int i = 0; i < percentList.size(); ++i) {
			// controlRDD: Associate ID | % failure | fail (0 or 1 where 0 is fail)
			double d = percentList.get(i);

			// Find those who were dropped and had a fail chance above our threshold
			// (successful prediction)
			long accurateFailedCount = controlRDD.filter(row -> row.getInt(2) == 0 && row.getDouble(1) >= d).count();
			// Find those who passed and had a fail chance below our threshold (successful
			// prediction)
			long accuratePassedCount = controlRDD.filter(row -> row.getInt(2) != 0 && row.getDouble(1) < d).count();
			long accurateCount = accurateFailedCount + accuratePassedCount;

			if (accurateCount > optimalAccurateCount) {
				optimalAccurateCount = accurateCount;
				optimalPercent = percentList.get(i);
			}
		}
		return new OptimalPoint(optimalPercent, optimalAccurateCount, controlRDD.count());
	}

	/**
	 * Returns MAE (Mean Absolute Error) of chance to fail
	 * EX1: if chance to fail is 0.70 but battery passed with 1, absolute error is
	 * 0.70.
 	 * EX2: if chance to fail is 0.70 but battery failed with 0, absolute error is
	 * 0.30.
	 * 
	 * @param results output from applyControlModel:
	 *                1st column is battery id
	 *                2nd column should be % chance to fail (0.0 = 0% to 1.0 = 100%)
	 *                3rd column should be either 0 for fail or 1 for pass
	 * @param [bounds=null] bounds to filter by. Excludes data that falls within bounds.
	 * @return MAE of RDD chance to fail
	 */
	public static double testMAE(JavaRDD<Row> results, double[] bounds) {
		if (bounds == null) {
			return results.mapToDouble(row -> Math.abs(new Double(row.getInt(2)) - 1.0d + row.getDouble(1))).mean();
		} else {
			JavaRDD<Row> boundedResults = results
					.filter(row -> (row.getDouble(1) < bounds[0] || row.getDouble(1) > bounds[1]));
			return boundedResults.mapToDouble(row -> Math.abs(new Double(row.getInt(2)) - 1.0d + row.getDouble(1)))
					.mean();
		}
	}

	/**
	 * Works like {@link {ModelApplier#testMAE(JavaRDD<Row>, double[])} with no boundaries (no data excluded)
	 * 
	 * @see ModelApplier#testMAE(JavaRDD<Row>, double[])
	 */
	public static double testMAE(JavaRDD<Row> results) {
		return testMAE(results, null);
	}

	/**
	 * Returns RMSE (Root Mean Squared Error) of chance to fail
	 * 
	 * @param results output from applyControlModel:
	 *                1st column is battery id
	 *                2nd column should be % chance to fail (0.0 = 0% to 1.0 = 100%)
	 *                3rd column should be either 0 for fail or 1 for pass
	 * @param [bounds=null] bounds to filter by. Excludes data that falls within bounds.
	 * @return RMSE of RDD chance to fail
	 */
	public static double testRMSE(JavaRDD<Row> results, double[] bounds) {
		if (bounds == null) {
			return Math.sqrt(results
					.mapToDouble(row -> Math.pow((new Double(row.getInt(2)) - 1.0d + row.getDouble(1)), 2.0d)).mean());
		} else {
			JavaRDD<Row> boundedResults = results
					.filter(row -> (row.getDouble(1) < bounds[0] || row.getDouble(1) > bounds[1]));
			return Math.sqrt(boundedResults
					.mapToDouble(row -> Math.pow((new Double(row.getInt(2)) - 1.0d + row.getDouble(1)), 2.0d)).mean());
		}
	}

	/**
	 * Works like {@link testRMSE(JavaRDD<Row>, double[])} with no boundaries (no data excluded)
	 * 
	 * @see testRMSE(JavaRDD<Row>, double[])
	 */
	public static double testRMSE(JavaRDD<Row> results) {
		return testRMSE(results, null);
	}
}