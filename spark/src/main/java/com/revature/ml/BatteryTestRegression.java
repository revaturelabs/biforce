package com.revature.sql;

import com.revature.util.DataFrameFactory;
import org.apache.log4j.Logger;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class BatteryTestRegression {

  private static final Logger LOGGER = Logger.getLogger(BatteryTestRegression.class);

  public void execute(SparkSession spark, String inputPath, String outputPath) {
    SparkContext context = spark.sparkContext();
    JavaRDD<String> batteryRDD = context.textFile(inputPath, 1).toJavaRDD();
    Dataset<Row> batteryDataFrame = DataFrameFactory.getBatteryDataFrame(spark, batteryRDD);
    Dataset<Row> testDataset = batteryDataFrame
        .filter("BATTERY_STATUS = 1 OR BATTERY_STATUS = 0");
    Dataset<Row> period1Type1Dataset = testDataset
        .filter("TEST_TYPE = 1 AND TEST_PERIOD = 1");
    Dataset<Row> period2Type1Dataset = testDataset
        .filter("TEST_TYPE = 1 AND TEST_PERIOD = 2");
    Dataset<Row> period3Type1Dataset = testDataset
        .filter("TEST_TYPE = 1 AND TEST_PERIOD = 3");
    Dataset<Row> period1Type2Dataset = testDataset
        .filter("TEST_TYPE = 2 AND TEST_PERIOD = 1");
    Dataset<Row> period2Type2Dataset = testDataset
        .filter("TEST_TYPE = 2 AND TEST_PERIOD = 2");
    Dataset<Row> period3Type2Dataset = testDataset
        .filter("TEST_TYPE = 2 AND TEST_PERIOD = 3");
    Dataset<Row> earlyTestDataset = period1Type1Dataset
        .select("BATTERY_ID", "BATTERY_STATUS", "TEST_SCORE")
        .join(period2Type1Dataset.select("BATTERY_ID", "TEST_SCORE"), "BATTERY_ID")
        .join(period3Type1Dataset.select("BATTERY_ID", "TEST_SCORE"), "BATTERY_ID")
        .join(period1Type2Dataset.select("BATTERY_ID", "TEST_SCORE"), "BATTERY_ID")
        .join(period2Type2Dataset.select("BATTERY_ID", "TEST_SCORE"), "BATTERY_ID")
        .join(period3Type2Dataset.select("BATTERY_ID", "TEST_SCORE"), "BATTERY_ID");
    earlyTestDataset.show();
    JavaRDD<LabeledPoint> features = earlyTestDataset.toJavaRDD().map(
        row -> {
          double batteryStatus = row.getDouble(1);
          double[] values = new double[]{
              row.getDouble(2)
              , row.getDouble(3)
              , row.getDouble(4)
              , row.getDouble(5)
              , row.getDouble(6)
              , row.getDouble(7)};
          return new LabeledPoint(batteryStatus, new DenseVector(values));
        });
    JavaRDD<LabeledPoint>[] splitData = features.randomSplit(new double[]{.6, .4});
    JavaRDD<LabeledPoint> trainingData = splitData[0].cache();
    JavaRDD<LabeledPoint> testData = splitData[1];
    LogisticRegressionModel model =
        new LogisticRegressionWithLBFGS().setNumClasses(2).run(trainingData.rdd());
    double trainingError = testData
        .mapToDouble(result -> {
          double diff = result.label() - model.predict(result.features());
          return Math.abs(diff);
        }).mean();
    LOGGER.info("Test error: " + trainingError);
    LOGGER.debug(testData.count());
    System.out.println(trainingError);
  }
}
