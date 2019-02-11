chmod 755 /media/sf_SharedWorkspace/Project3/biforce/spark/BatteryAnalysis/target/SparkAnalysis.jar
spark-submit --master yarn --class com.revature.Driver SparkAnalysis.jar HData/battery_test.csv p3out/spark-output.txt
&& aws s3 cp spark-output.txt s3://revature-analytics-dev/spark-output
