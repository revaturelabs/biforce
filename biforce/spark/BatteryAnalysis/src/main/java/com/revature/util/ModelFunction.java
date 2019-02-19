package com.revature.util;

import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

public class ModelFunction{

	public static double[][] execute(Dataset<Row> csv, List<List<Double>> partitions){
		double[][] model = new double[3][4];


		model[0] = logReg(statsDS(binDS(modelDS(csv, "_c1", 1), partitions.get(0))), 1);
		System.out.println("MODEL 1 DONE");
		model[1] = logReg(statsDS(binDS(modelDS(csv, "_c1", 2), partitions.get(1))), 2);
		System.out.println("MODEL 2 DONE");
		model[2] = logReg(statsDS(binDS(modelDS(csv, "_c1", 3), partitions.get(2))), 3);
		System.out.println("MODEL 3 DONE");
		//		model[3] = logReg(statsDS(binDS(modelDS(csv, "_c1", 4), partitions.get(3))), 4);
		//		model[4] = logReg(statsDS(binDS(modelDS(csv, "_c4", 1))), 5);
		//		model[5] = logReg(statsDS(binDS(modelDS(csv, "_c4", 2))), 6);
		//		model[6] = logReg(statsDS(binDS(modelDS(csv, "_c4", 3))), 7);

		return model;
	}

	private static Dataset<Row> modelDS(Dataset<Row> data, String modelType, int intType) {
		Dataset<Row> modelTypeDS =  data.filter(modelType+" = "+intType).groupBy("_c9").mean("_c3").withColumnRenamed("avg(_c3)", "avg_score");
		Dataset<Row> status = data.filter(modelType+" = "+intType).groupBy("_c9").max("_c10").withColumnRenamed("max(_c10)", "status").withColumnRenamed("_c9", "assc1");

		Dataset<Row> result = modelTypeDS.join(status, modelTypeDS.col("_c9").equalTo(status.col("assc1"))).select("_c9","avg_score","status");

		return result;

	}

	private static Dataset<Row> binDS(Dataset<Row> input, List<Double> partitions) {
		partitions.add(100.0); // final value
		Dataset<Row> bins = input.filter("avg_score < " + partitions.get(0)).withColumn("bin", functions.lit(1));
		int binNum = 2;
		for(int i = 1; i <= 9; i++){
			Dataset<Row> bin;


			bin = input.filter("avg_score >= " + partitions.get(i-1) + " and avg_score < "+ partitions.get(i)).
					withColumn("bin", functions.lit(binNum));

			bins = bins.union(bin); // union all
			binNum++;
		}
		return bins;
	}

	private static double[][] statsDS(Dataset<Row> input) {
		double prob, logOdds;

		int[][] counts = new int[10][];
		double[][] probs = new double[10][3];

		List<Row> inputList = input.collectAsList();

		for(int i = 0; i < 10; i++) {
			counts[i] = new int[]{i, 0, 0};
		}
		int rowNum = 0;
		for(Row row : inputList) {
			int binNum = row.getInt(3);
			int status = row.getInt(2);
			counts[binNum-1][1]++;
			if(status == 0) counts[binNum-1][2]++;
			rowNum++;
		}
		for(int i = 0; i < 10; i++) {
			int binTotal = counts[i][1];
			int binDropped = counts[i][2];
			if((binTotal - binDropped) < 1 || binDropped == 0 || binTotal == 0){
				prob = (double) -1;
				logOdds = (double) -1;
			} else{
				prob = (((double) binDropped)/binTotal*100);
				logOdds = Math.log((double) binDropped / (binTotal - binDropped));
			}

			probs[i] = new double[] {i+1, prob, logOdds};
		}

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

		double Sx, Sy, Sxy, Sx2, Sy2, m, b, r2;
		double[] modelData = new double[4];
		Sx = Sy = Sxy = Sx2 = Sy2 = 0;
		int n = 0;

		for(int i = 0; i < stats.length; i++) {
			double x = stats[i][0];
			double y = stats[i][1];
			if(y != -1) {
				Sx += x;
				Sy += y;
				Sx2 += x*x;
				Sy2 += y*y;
				Sxy += x*y;
				n++;
			}
		}

		m = (n*Sxy - Sx*Sy)/(n*Sx2 - Sx*Sx);
		b = (Sy*Sx2 - Sx*Sxy)/(n*Sx2 - Sx*Sx);
		r2 = (n*Sxy - Sx*Sy)*(n*Sxy - Sx*Sy)/((n*Sx2 - Sx*Sx)*(n*Sy2 - Sy*Sy));

		modelData = new double[]{modelNum, m, b, r2};
		return modelData;
	}

	/*
	private static Dataset<Row> statsDS(Dataset<Row> input) {
		Column logOdds, binTotal, binDropped;

		Dataset<Row> totalCount = input.groupBy("bin").count().
				withColumnRenamed("bin", "bin1").withColumnRenamed("count", "total");
		Dataset<Row> droppedCount = input.filter("status = 0").groupBy("bin").count().
				withColumnRenamed("count", "dropped");
		Dataset<Row> Counts = totalCount.join(droppedCount,droppedCount.col("bin").equalTo(totalCount.col("bin1"))).
				withColumnRenamed("total", "binTotal").withColumnRenamed("dropped", "binDropped").
				select("bin", "binTotal", "binDropped");

		binTotal = Counts.col("binTotal");
		binDropped = Counts.col("binDropped");
		logOdds = functions.log(binDropped.divide(binTotal.minus(binDropped)));

		Dataset<Row> statsDS = Counts.withColumn("logOdds", logOdds).withColumnRenamed("bin", "bin1");

		Dataset<Row> stats = input.join(statsDS,statsDS.col("bin1").equalTo(input.col("bin"))).
				select("avg_score", "logOdds");

		return stats;
	}

	private static double[] logReg(Dataset<Row> stats, int modelNum) {
		Dataset<Row> statsF = stats.filter("logOdds is not null").withColumn("n", functions.lit(1));
		Column x = statsF.col("avg_score");
		Column y = statsF.col("logOdds");
		Dataset<Row> statsS = statsF.
				withColumn("xy", x.multiply(y)).
				withColumn("x2", x.multiply(x)).
				withColumn("y2", y.multiply(y)).groupBy().sum();

		double Sx, Sy, Sxy, Sx2, Sy2, m, b, r2;
		Sx = Sy = Sxy = Sx2 = Sy2 = 0;
		long n = 0;

		List<Row> sumList = statsS.toJavaRDD().collect();

		for(Row row : sumList) {
			Sx = row.getDouble(0);
			Sy = row.getDouble(1);
			n = row.getLong(2);
			Sxy = row.getDouble(3);
			Sx2 = row.getDouble(4);
			Sy2 = row.getDouble(5);
		}

		m = (n*Sxy - Sx*Sy)/(n*Sx2 - Sx*Sx);
		b = (Sy*Sx2 - Sx*Sxy)/(n*Sx2 - Sx*Sx);
		r2 = (n*Sxy - Sx*Sy)*(n*Sxy - Sx*Sy)/((n*Sx2 - Sx*Sx)*(n*Sy2 - Sy*Sy));

		double[] modelData = new double[]{modelNum, m, b, r2};
		return modelData;
	}
	 */
}