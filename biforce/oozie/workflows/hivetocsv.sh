#!/bin/bash
hive -e "insert overwrite local directory '$1'
row format delimited fields terminated by ','
select * from biforce_staging.caliber_assessment"
cat $1/* > $2/caliber_assessment.csv

hive -e "insert overwrite local directory '$1'
row format delimited fields terminated by ','
select * from biforce_staging.caliber_batch"
cat $1/* > $2/caliber_batch.csv

hive -e "insert overwrite local directory '$1'
row format delimited fields terminated by ','
select * from biforce_staging.caliber_grade"
cat $1/* > $2/caliber_grade.csv

hive -e "insert overwrite local directory '$1'
row format delimited fields terminated by ','
select * from biforce_staging.caliber_note"
cat $1/* > $2/caliber_note.csv

hive -e "insert overwrite local directory '$1'
row format delimited fields terminated by ','
select * from biforce_staging.caliber_trainee"
cat $1/* > $2/caliber_trainee.csv
