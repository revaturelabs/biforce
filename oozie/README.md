## Oozie Team
Sqoop workflow

1) Before running the spark workflow, the mySQL JDBC connection driver must be installed.
https://www.cloudera.com/documentation/enterprise/5-14-x/topics/cdh_ig_sqoop_installation.html#topic_13
https://www.cloudera.com/documentation/enterprise/5-14-x/topics/cdh_oozie_sqoop_jdbc.html
Above are the general instructions, make sure the mysql JDBC driver is placed in hortonwork hdfs's /user/oozie/libext

2) Change the hortonPass parameter in job.properties file equal to the password for root user in your mySQL.

3) Change 'mySQLPW' in sqoop-job.txt to mySQL password for root user in mySQL

4) Run sqoop-job.txt on command to add sqoop job for join command

5) use -copyFromLocal command to migrate workflow.xml to directory of choice; Here we have it set to /user/root/HData

6) Run the following command while in the local directory that contains job.properties:

oozie job -oozie http://sandbox-hdp.hortonworks.com:11000/oozie -D oozie.wf.application.path /user/root/HData/workflow.xml  -config job.properties -run


* Add documentation here
* Add documentation here
* Add documentation here
