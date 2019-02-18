#!/bin/sh
#Editing this file in windows will cause it to not work when running the commands in linux. To fix this, run the command 'dos2unix setup.sh' in linux cli before running the script.
echo [CONFIGURING BIFORCE DIRECTORIES IN HDFS]
hadoop fs -rm -r biforce
hadoop fs -mkdir biforce
hadoop fs -mkdir biforce/lib
hadoop fs -mkdir biforce/workflows
hadoop fs -mkdir biforce/coordinators
hadoop fs -mkdir biforce/bundles
hadoop fs -put ojdbc6.jar biforce/lib
hadoop fs -put workflows/setup-workflow.xml biforce/workflows
hadoop fs -put workflows/etl-workflow.xml biforce/workflows
#
echo [STARTING OOZIE SETUP WORKFLOW]
#The oozie workflow configured in setup-job.properties assumes that you have a sqoop metastore server running. The current iteration uses 
oozie job -oozie http://quickstart.cloudera:11000/oozie -run -config properties/setup-job.properties