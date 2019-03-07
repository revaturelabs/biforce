package com.revature.util;

import java.util.List;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

public class ModelFunctionDataset {
	
	public static double[][] execute(Dataset<Row> csv, List<List<Double>> partitions, int splitCount){
		double[][] model = new double[3][4];

		System.out.println("Starting to build model...");
		// Builds verbal score model
		Dataset<Row> model1 = logReg(statsDS(binDS(modelDS(csv, 1), partitions.get(0),splitCount),splitCount), 1);
		System.out.println("MODEL 1 DONE");
		// Builds exam score model
		Dataset<Row> model2 = logReg(statsDS(binDS(modelDS(csv, 2), partitions.get(1),splitCount),splitCount), 2);
		System.out.println("MODEL 2 DONE");
		// Builds project score model
		Dataset<Row> model3 = logReg(statsDS(binDS(modelDS(csv, 3), partitions.get(2),splitCount),splitCount), 3);
		System.out.println("MODEL 3 DONE");
		
		model = collectModels(model1.union(model2).union(model3));
		return model;
	}
	
	private static Dataset<Row> modelDS(Dataset<Row> data, int testType) {
		// Filters the data based on test type and finds the mean score and status for an individual associate
		Dataset<Row> modelTypeDS =  data.filter("_c1 = "+testType).groupBy("_c9").mean("_c3").withColumnRenamed("avg(_c3)", "avg_score");
		Dataset<Row> status = data.filter("_c1 = "+testType).groupBy("_c9").max("_c10").withColumnRenamed("max(_c10)", "status").withColumnRenamed("_c9", "assc1");

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
	
	private static Dataset<Row> statsDS(Dataset<Row> input, int numBins) {

		Column logOdds, binTotal, binDropped;
		
		Dataset<Row> totalCount = input.groupBy("bin").count().
				withColumnRenamed("bin", "bin1").
				withColumnRenamed("count", "total");
		Dataset<Row> droppedCount = input.filter("status = 0").groupBy("bin").count().
				withColumnRenamed("count", "dropped");
		Dataset<Row> Counts = totalCount.join(droppedCount).
				where(droppedCount.col("bin").$eq$eq$eq(totalCount.col("bin1"))).
				withColumnRenamed("total", "binTotal").
				withColumnRenamed("dropped","binDropped").
				select("bin", "binTotal", "binDropped");
		
		binTotal = Counts.col("binTotal");
		binDropped = Counts.col("binDropped");
		logOdds = functions.log(binDropped.divide(binTotal.minus(binDropped)));
		
		Dataset<Row> statsDS = Counts.withColumn("logOdds", logOdds).
				withColumnRenamed("bin", "bin1");
		Dataset<Row> stats = input.join(statsDS).where(statsDS.col("bin1").
				$eq$eq$eq(input.col("bin"))).
				select("avg_score", "logOdds");
		System.out.println("statsDS");
		return stats;
	}
	
	private static Dataset<Row> logReg(Dataset<Row> stats, int modelNum) {


		Dataset<Row> statsF = stats.filter("logOdds is not null").withColumn("n", 
				functions.lit(1));
		Column x = statsF.col("avg_score");
		Column y= statsF.col("logOdds");
		Dataset<Row> statsS = statsF.
				withColumn("xy", x.multiply(y)).
				withColumn("x2", x.multiply(x)).
				withColumn("y2", y.multiply(y)).groupBy().sum();
		
		Column Sx, Sy, Sxy, Sx2, Sy2, m, b, r2, n;
		Sx = statsS.col("sum(avg_score)");
		Sy = statsS.col("sum(logOdds)");
		Sxy = statsS.col("sum(xy)");
		Sx2 = statsS.col("sum(x2)");
		Sy2 = statsS.col("sum(y2)");
		n = statsS.col("sum(n)");
		m = (n.multiply(Sxy).minus(Sx.multiply(Sy))).
				divide(n.multiply(Sx2).minus(Sx.multiply(Sx)));
		b = (Sy.multiply(Sx2).minus(Sx.multiply(Sxy))).
				divide(n.multiply(Sx2).minus(Sx.multiply(Sx)));
		r2 = (n.multiply(Sxy).minus(Sx.multiply(Sy))).
				multiply(n.multiply(Sxy).minus(Sx.multiply(Sy))).
				divide((n.multiply(Sx2).minus(Sx.multiply(Sx))).
						multiply(n.multiply(Sy2).minus(Sy.multiply(Sy))));
		Dataset<Row> model = statsS.withColumn("m", m).
				withColumn("b", b).withColumn("r2", r2).
				withColumn("model", functions.lit(modelNum)).
				select("model", "m", "b", "r2");
		
		System.out.println("Model: "+ modelNum);
		
		return model;
	}
	
	private static double[][] collectModels(Dataset<Row> models){
		double[][] results = new double[3][4];
		List<Row> modelArray = models.collectAsList();
		System.out.println("Collect");
		int i = 0;
		for(Row row: modelArray) {
			results[i] = new double[]{(double) row.getInt(0), row.getDouble(1), row.getDouble(2), row.getDouble(3)};
			i++;
			System.out.println("Array Write");
		}
		return results;
}
}
