#!/bin/sh
echo [SETUP START]
#Editing this file in windows will cause it to not work when running the commands in linux. To fix this, run the command 'dos2unix setup.sh' in linux cli before running the script.
echo [CONFIGURING BIFORCE DIRECTORIES IN HDFS]
echo Removing old biforce directory.
hadoop fs -rm -r biforce
echo Creating biforce/ directory.
hadoop fs -mkdir biforce
echo Creating biforce/lib subdirectory.
hadoop fs -mkdir biforce/lib
echo Creating biforce/workflows subdirectory.
hadoop fs -mkdir biforce/workflows
echo Creating biforce/coordinators subdirectory.
hadoop fs -mkdir biforce/coordinators
echo Creating biforce/bundles subdirectory
hadoop fs -mkdir biforce/bundles
echo Putting ojdbc6.jar to biforce/lib.
hadoop fs -put ojdbc6.jar biforce/lib
echo Putting setup-workflow.xml to biforce/workflows.
hadoop fs -put workflows/setup-workflow.xml biforce/workflows
echo Putting etl-workflow.xml to biforce/workflows.
hadoop fs -put workflows/etl-workflow.xml biforce/workflows
#
echo [STARTING OOZIE SETUP WORKFLOW]
#The oozie workflow configured in setup-job.properties assumes that you have a sqoop metastore server running. The current iteration uses 
oozie job -oozie http://quickstart.cloudera:11000/oozie -run -config properties/setup-job.properties
echo [SETUP END]