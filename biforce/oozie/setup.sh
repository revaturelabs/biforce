#!/bin/sh
#
#WARNING: This file should only be edited in the Linux/Unix development environment. Editing it in Windows will alter the internal formatting and cause an error when executing. If altered in Windows, run 'dos2Unix setup.sh' in the Linux/Unix command line to convert the file back to the correct formatting.
#
#TODO:
#Implement oozie job run of biforce-setup.xml at end of script
#
echo ===================================================================================
echo =============================== BIFORCE SETUP START ===============================
echo ===================================================================================
#The main biforce directory stores all Biforce application files in HDFS. This script wipes any pre-existing instance of the biforce/ directory to ensure that there are no artifacts from testing. This means that this script should only be run once during the beginning of production phase to ensure that there is no loss of Sqoop jobs, or incremental import values.
echo Configuring HDFS directories
echo Deleting pre-existing [biforce/]
hadoop fs -rm -r biforce
echo Creating [biforce/]
hadoop fs -mkdir biforce
echo -----------------------------------------------------------------------------------
#The lib directory stores all drivers required to run the application. The main job.properties file for Oozie workflows points to this location for drivers. If you need to use additional drivers, remember to add them to this directory as well as point to it.
echo Creating [biforce/lib]
hadoop fs -mkdir biforce/lib
echo Adding [ojdbc6.jar] to [biforce/lib]
hadoop fs -put ojdbc6.jar biforce/lib
echo Adding [postgresql-java7.jar] to [biforce/lib]
hadoop fs -put postgresql-java7.jar biforce/lib
echo -----------------------------------------------------------------------------------
#The workflows directory holds all Oozie workflows and is currently divided into setup and execution subdirectories
echo Creating [biforce/workflows]
hadoop fs -mkdir biforce/workflows
echo -----------------------------------------------------------------------------------
#The setup directory contains all Oozie workflows used in the initial setup of the application onto HDFS. It is currently divided into OLAP and warehouse subdirectories.
echo Creating [biforce/workflows/setup]
hadoop fs -mkdir biforce/workflows/setup
echo Adding [biforce-setup.xml] to [biforce/workflows/setup]
hadoop fs -put workflows/setup/biforce-setup.xml biforce/workflows/setup
echo Adding [biforce-setup.properties] to [biforce/workflows/setup]
hadoop fs -put workflows/setup/biforce-setup.properties biforce/workflows/setup
echo -----------------------------------------------------------------------------------
#The OLAP directory pertains to all setup workflows that are required for importing data from Caliber directly into Hive. See Hive workflows for more information.
echo Creating [biforce/workflows/setup/OLAP]
hadoop fs -mkdir biforce/workflows/setup/OLAP
echo Adding [create-hive-imports.xml] to [biforce/workflows/setup/OLAP]
hadoop fs -put workflows/setup/OLAP/create-hive-imports.xml biforce/workflows/setup/OLAP
echo Adding [delete-hive-imports.xml] to [biforce/workflows/setup/OLAP]
hadoop fs -put workflows/setup/OLAP/delete-hive-imports.xml biforce/workflows/setup/OLAP
echo Adding [hive-create.hql] to [biforce/workflows/setup/OLAP]
hadoop fs -put workflows/setup/OLAP/hive-create.hql biforce/workflows/setup/OLAP
echo -----------------------------------------------------------------------------------
#The warehouse directory holds setup workflows that use Sqoop commands to import data from Caliber to HDFS to conform with the warehouse schema. See warehouse workflows for more information.
echo Creating [biforce/workflows/setup/warehouse]
hadoop fs -mkdir biforce/workflows/setup/warehouse
echo Adding [create-warehouse-imports.xml] to [biforce/workflows/setup/warehouse]
hadoop fs -put workflows/setup/Warehouse/create-warehouse-imports.xml biforce/workflows/setup/warehouse
echo Adding [delete-warehouse-imports.xml] to [biforce/workflows/setup/warehouse]
hadoop fs -put workflows/setup/Warehouse/delete-warehouse-imports.xml biforce/workflows/setup/warehouse
echo -----------------------------------------------------------------------------------
#The execution directory contains all Oozie workflows used during runtime of actual application. See each workflow for more information.
echo Creating [biforce/workflows/execution]
hadoop fs -mkdir biforce/workflows/execution
echo Adding [execute-hive-imports.xml] to [biforce/workflows/execution]
hadoop fs -put workflows/execution/execute-hive-imports.xml biforce/workflows/execution
echo Adding [execute-warehouse-imports.xml] to [biforce/workflows/execution]
hadoop fs -put workflows/execution/execute-warehouse-imports.xml biforce/workflows/execution
echo
echo REQUIRED MANUAL STEPS:
echo -----------------------------------------------------------------------------------
echo Caliber password alias must exist in this machine in order to use Oozie import workflows.
echo
echo If password already exists and is still correct, ignore this message.
echo
echo To create new alias, use the following command and enter new password when prompted:
echo [hadoop credential create caliber.password.alias -provider jceks://hdfs/user/root/caliber.password.jceks]
echo
echo To delete old alias, use the following command:
echo [hadoop credential delete caliber.password.alias -provider jceks://hdfs/user/root/caliber.password.jceks]
echo -----------------------------------------------------------------------------------
echo Oozie job biforce-setup.xml must be manually run to complete setup.
echo
echo Use the following command in order to run job:
echo oozie job --oozie http://[nameNode][port#]/oozie -run -config [directory]/biforce-setup.properties