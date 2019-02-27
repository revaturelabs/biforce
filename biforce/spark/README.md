## Spark Team
* The current code in SparkAnalysis takes 4 arguments:
  the spark master, the input file, the output file, and a control data output file.
  
  
  On an EMR, in the same directory as the jar, run the code with
  `spark-submit --master yarn --class com.revature.Driver Biforce_Analysis.jar p3in/spark_input.csv sparkOutput controlOutput`
  where spark_input.csv is in the HDFS under ~/p3in
  Output is written to an s3 bucket automatically (consider changing this and just specifying the s3 in the input)
  
  Locally on Cloudera or HortonWorks, create a directory p3out, and `touch` files sparkOutput.txt and controlOutput.txt
  Set s3Location to an empty string.
  Run `spark-submit --class com.revature.Driver Biforce_Analysis.jar p3in/spark_input.csv p3out/sparkOutput.txt p3out/controlOutput.txt`
  
  
* The only useful data for model building is associates that are employed or dropped. Those in training are filtered out.
* This data is then split (70/30) into model and control data, for building the model and testing its accuracy.

* The input file contains 11 columns, of which 5 are used. _c1:test_type, _c3:score, _c4:test period, _c9:associate id, _c10:associate status
..*If this is changed in the future, column names as well as RDD row indices will have to be changed. I do not believe it has a significant effect on performance.
* The output file (sparkOutput) contains 4 columns with no header: associate_id,% Chance to Fail,Most Recent Week,Prediction
* The control output file is used for determining week-by-week accuracy, and logging of the equations.
..*These equations are used to find partial chances of being dropped.
..*These partial chances are weighted according to the strength of their test's correlation to the drop chance.
..*They are finally combined into a single drop chance.
..*The prediction is made by splitting the drop chances control data at the point with the least number of incorrect predictions.

Future iteration considerations:
- Further optimize ModelFunction for accuracy and (potentially) speed. 
..*Currently 90% accurate by week 4 with 850 associates, 170 of which are confirmed or dropped.
..*Currently takes about 2.5 min to run.
- Allow for appending of output files, rather than overriding.