## Oozie Team

#Running the Spark WordCount example through Oozie

This oozie workflow was ran on Hortonworks. 
In order to view the hadoop jobhistory logs, you may want to login as root.
For Spark2 compatability: https://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.6.0/bk_spark-component-guide/content/ch_oozie-spark-action.html#spark-config-oozie-spark2
 
* Make a directory in HDFS (HData) that will hold your workflow and the input for your spark action.
  
```
hdfs dfs -mkdir /user/root/HData
hdfs dfs -copyFromLocal workflow.xml HData/
hdfs fds -copyFromLocal all-bible HData/
```
* Make a directory in HDFS at the same location as the workflow.xml.
* Note: it must be named "lib".

```
hdfs dfs -mkdir /user/root/HData/lib
hdfs dfs -copyFromLocal WordCountSpark.jar HData/lib
```

* Use the following to run the oozie workflow:

```
oozie job -oozie http://sandbox-hdp.hortonworks.com:11000/oozie -config <local path of job.properties> -run
```


#Sqoop...
* Add documentation here
