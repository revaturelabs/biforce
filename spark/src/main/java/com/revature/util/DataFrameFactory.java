package com.revature.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class DataFrameFactory {

  private static final String schemaString = "TEST_ID TEST_TYPE TEST_RAW_SCORE TEST_SCORE TEST_PERIOD TEST_CATEGORY BUILDER_ID GROUP_ID GROUP_TYPE_ID BATTERY_ID BATTERY_STATUS";

  public static Dataset<Row> getBatteryDataFrame(SparkSession spark, JavaRDD<String> batteryRDD) {
    List<StructField> fields = new ArrayList<>();
    for (String fieldName : schemaString.split(" ")) {
      StructField field = DataTypes.createStructField(fieldName, DataTypes.DoubleType, true);
      fields.add(field);
    }
    StructType schema = DataTypes.createStructType(fields);

    JavaRDD<Row> batteryRowRDD = batteryRDD.map((Function<String, Row>) record ->
    {
      // Declare the reference as Object[] so that we can pass it as varargs.
      String[] attributes = record.split(",");

      List<Double> numericAttributes = new ArrayList<>();
      for (String attribute: attributes) {
        numericAttributes.add(Double.parseDouble(attribute));
      }
      return RowFactory.create((Object[]) numericAttributes.toArray());
    });

    return spark.createDataFrame(batteryRowRDD, schema);
  }
}
