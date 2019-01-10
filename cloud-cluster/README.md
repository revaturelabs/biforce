# Cloud Cluster Team

## Configuration and code to run a spark job in the EMR cluster

If EMR key is encrypted then decrypt it to allow passwordless ssh login.

* Copy secure key to id_rsa.
```
cp emr-secure-key.pem id_rsa
```
Decrypt the copied key
```
ssh-keygen -p -f id_rsa
```
Move id_rsa to .ssh directory 
```
mv id_rsa ~/.ssh/id_rsa 
```
Create an ssh-action within an Oozie workflow. 

``` xml
 <action name="oozie-ssh">
   <ssh xmlns="uri:oozie:ssh-action:0.1">
       <host>${emr_hostname}</host>
       <command>/home/hadoop/test.sh 2> /home/hadoop/SparkCommand.log</command>
 	       <capture-output/>
    </ssh>
    <ok to="end"/>
    <error to="kill"/>
 </action>
```

Copy the output of the command and use it for localhost in name_node and job_tracker
```
hostname -f
```
Specify the hostname and localhost in the job.properties file. 

``` conf
#Configuration Parameters
name_node = hdfs://localhost:8020
job_tracker = localhost:8032
emr_hostname = emr_hostname@emr_ipaddress
Oozie.wf.application.path = /path_to_workflow_in_hdfs
Oozie.use.system.libpath = true
Oozie.action.ssh.allow.user.at.host = true
```
