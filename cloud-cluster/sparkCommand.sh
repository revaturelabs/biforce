chmod 755 /home/hadoop/spark/SparkAnalysis.jar
spark-submit --master yarn --class com.revature.Driver SparkAnalysis.jar HData/battery_test.csv output