#!/bin/sh
echo [ETL SETUP STARTED]
echo
echo [CREATING PASSWORD ALIAS FOR CALIBER SEE etl-setup.sh FOR INFO] #The password alias allows sqoop jobs to have the log in credentials for caliber without hard coding the actual password into the script. This assumes that the alias hasn't already been created. If the alias already exists, you can allow this step to fail and the rest of the script will continue as planned. If the underlying password has changed, you can delete the alias by using the command [hadoop credential delete caliber.password.alias -provider jceks://hdfs/user/root/mysql.password.jceks] and then running the below command manually.
echo MANUAL STEP: Wait for the prompt, and then enter the caliber password
hadoop credential create caliber.password.alias -provider jceks://hdfs/user/root/mysql.password.jceks
echo
echo [SETTING UP HDFS DIRECTORIES FOR BIFORCE]
hadoop fs -rm -r biforce
hadoop fs -mkdir biforce/
hadoop fs -mkdir biforce/lib
hadoop fs -mkdir biforce/workflows
echo
echo [ATTEMPTING TO MOVE ORACLE DRIVER JAR FROM LOCAL FILE. SEE etl-setup.sh FOR INFO] #This step assumes that you have the ojdbc6.jar driver in your user home directory. If you  need a different version of the driver, you can manually put it into the biforce/lib directory.
hadoop fs -put ojdbc6.jar biforce/lib
hadoop fs -put setup-workflow.xml biforce/workflows
echo
echo [RUNNING OOZIE SETUP WORKFLOW]
oozie job -oozie http://quickstart.cloudera:11000/oozie -run -config setup-job.properties