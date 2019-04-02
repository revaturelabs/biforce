#!/bin/bash
export AWS_ACCESS_KEY_ID=$3
export AWS_SECRET_ACCESS_KEY=$4
aws s3 rm s3://revature-analytics-dev/sparkOutput --recursive
aws s3 rm s3://revature-analytics-dev/controlOutput --recursive
scp -r $1/biforce/oozie/workflows/csv/ $2:~/
ssh $2 hadoop fs -rm -r csv
ssh $2 hadoop fs -mkdir csv
ssh $2 hadoop fs -put csv/spark_data.csv csv
ssh $2 spark-submit --master yarn --class com.revature.Driver Biforce_Analysis.jar csv/spark_data.csv sparkOutput controlOutput
