INSERT OVERWRITE DIRECTORY '/user/cloudera/Caliber_Out/address'
row format delimited fields terminated by '~' NULL DEFINED AS 'null'
select *
from caliber_address;

INSERT OVERWRITE DIRECTORY '/user/cloudera/Caliber_Out/assessment'
row format delimited fields terminated by '~' NULL DEFINED AS 'null'
select *
from caliber_assessment;

INSERT OVERWRITE DIRECTORY '/user/cloudera/Caliber_Out/batch'
row format delimited fields terminated by '~' NULL DEFINED AS 'null'
select *
from caliber_batch;

INSERT OVERWRITE DIRECTORY '/user/cloudera/Caliber_Out/category'
row format delimited fields terminated by '~' NULL DEFINED AS 'null'
select *
from caliber_category;

INSERT OVERWRITE DIRECTORY '/user/cloudera/Caliber_Out/grade'
row format delimited fields terminated by '~' NULL DEFINED AS 'null'
select *
from caliber_grade;

INSERT OVERWRITE DIRECTORY '/user/cloudera/Caliber_Out/note'
row format delimited fields terminated by '~' NULL DEFINED AS 'null'
select *
from caliber_note;

INSERT OVERWRITE DIRECTORY '/user/cloudera/Caliber_Out/trainee'
row format delimited fields terminated by '~' NULL DEFINED AS 'null'
select *
from caliber_trainee;

INSERT OVERWRITE DIRECTORY '/user/cloudera/Caliber_Out/trainer'
row format delimited fields terminated by '~' NULL DEFINED AS 'null'
select *
from caliber_trainer;

