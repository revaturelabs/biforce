create table battery_test(test_type integer, raw_score integer, score real, test_period integer, test_category integer,
builder_id integer, group_id integer, group_type integer, battery_id integer, battery_status integer);

copy battery_test
from 's3://revature-analytics-dev/battery_test.csv'
access_key_id '<S3 access key>'
secret_access_key '<S3 secret key>'
format as csv;

create table if not exists ml_output(battery_id integer not null, pass_fail decimal not null);

copy ml_output
from 's3://revature-analytics-dev/part-00000-10a17d42-4a75-461e-a93d-7b1fc9b8b269-c000.csv'
access_key_id '<S3 access key>'
secret_access_key '<S3 secret key>'
format as csv;

create table if not exists spark_output(battery_id integer not null, fail_chance real not null,
sample_size integer);

copy ml_output
from 's3://revature-analytics-dev/FinalOutput.csv'
access_key_id '<S3 access key>'
secret_access_key '<S3 secret key>'
format as csv;
