chmod 755 /home/hadoop/spark/SparkAnalysis.jar
spark-submit --master yarn --class com.revature.Driver SparkAnalysis.jar HData/battery_test.csv spark-output.txt \
&& aws s3 cp spark-output.txt s3://revature-analytics-dev/spark-output