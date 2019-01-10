## Oozie Team
Sqoop workflow

1) Before running the spark workflow, the mySQL JDBC connection driver must be installed.
https://www.cloudera.com/documentation/enterprise/5-14-x/topics/cdhigsqoopinstallation.html#topic13
https://www.cloudera.com/documentation/enterprise/5-14-x/topics/cdhooziesqoopjdbc.html
Above are the general instructions, make sure the mysql JDBC driver is placed in hortonwork hdfs's /user/oozie/libext

2) Change the hortonPass parameter in job.properties file equal to the password for root user in your mySQL.

3) Change 'mySQLPW' in sqoop-job.txt to mySQL password for root user in mySQL

4) Run sqoop-job.txt on command to add sqoop job for join command

5) use -copyFromLocal command to migrate workflow.xml to directory of choice; Here we have it set to /user/root/HData

6) Run the following command while in the local directory that contains job.properties:

```
oozie job -oozie http://sandbox-hdp.hortonworks.com:11000/oozie -D oozie.wf.application.path /user/root/HData/workflow.xml  -config job.properties -run
```
#Spark WordCount
This oozie workflow was ran on Hortonworks.                                                                                                                             
In order to view the hadoop jobhistory logs, you may want to login as root.                                                                                            
For Spark2 compatability: https://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.6.0/bkspark-component-guide/content/choozie-spark-action.html#spark-config-oozie-spark2
                                                                                                                                                                     
* Make a directory in HDFS (HData) that will hold your workflow and the input for your spark action.
                                                                                                                                                                       
```
hdfs dfs -mkdir /user/root/HData                                                                                                                                        
hdfs dfs -copyFromLocal workflow.xml HData/                                                                                               
hdfs dfs -copyFromLocal all-bible HData/                                                                                                                                
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
#Oozie Coordinators

* Make a directory in HDFS (CData) that will hold your coordinator
                                                                                                                                                                       
```
hdfs dfs -mkdir /user/root/CData                                                                                                                                        
hdfs dfs -copyFromLocal coordinator.xml CData/                                                                                                                        
```
* Change the application path in your job.properties file to point to the coordinator instead of the workflow.
* Use the same line as above to run the coordinator job.
                                                                                                                                                                        
```
oozie job -oozie http://sandbox-hdp.hortonworks.com:11000/oozie -config <local path of job.properties> -run                                                             
```



