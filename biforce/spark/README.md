## Spark Team
* The current code in SparkAnalysis takes 4 arguments:
  the spark master, the input file, the output file, and an optional
  batch number. If the batch number isn't provided it will analayze all batteries.
* The input file should point to battery_test.csv and the output will be in csv
  format. The csv file contains 3 columns: the battery_id, the %
  chance they will fail, and the sample size. The higher the sample
  size, the more accurate the prediction.

Future iteration considerations:
- Speed up the code using caching. At the moment it takes 6 hours to process 852 unique 
batteries. Example: `Dataset<String> logData = spark.read().textFile(logFile).cache();` [Caching in Spark](https://spark.apache.org/docs/latest/quick-start.html#caching)
- More indicators, at the moment only the first 3 test types in the first 3
periods are taken into consideration
- Include an accuracy value, currently the sample size gives an idea of reliability, but not a degree of error.
- Answer the specific question of who should be dropped, and who shouldn't.
- Get the dataset's foreach function to work instead of converting to a list
of rows.
