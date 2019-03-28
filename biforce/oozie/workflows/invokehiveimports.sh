#!/bin/bash
sqoop job --meta-connect $1 --exec hive-import-assessment

sqoop job --meta-connect $1 --exec hive-import-batch

sqoop job --meta-connect $1 --exec hive-import-grade

sqoop job --meta-connect $1 --exec hive-import-note

sqoop job --meta-connect $1 --exec hive-import-trainee

