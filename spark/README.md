## Spark Team
* The current code in SparkAnalysis takes 3 arguments:
* the spark master, the input file and output file. The input file
* should point to battery_test.csv and the output will be in csv
* format. The csv file contains 3 columns: the battery_id, the %
* chance they will fail, and the sample size. The higher the sample
* size, the more accurate the prediction.

Future iteration considerations:
-speed up the code, at the moment it takes 6 hours to process 852 unique 
batteries
-More indicators, at the moment only the first 3 test types in the first 3
periods are taken into consideration
-Include an accuracy value, currently the sample size gives an idea of reliability, but not a degree of error.
-Answer the specific question of who should be dropped, and who shouldn't.
-Get the dataset's foreach function to work instead of converting to a list
of rows.
