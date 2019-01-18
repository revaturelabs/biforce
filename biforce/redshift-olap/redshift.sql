create table battery_test(test_type integer, raw_score integer, score real, test_period integer, test_category integer,
builder_id integer, group_id integer, group_type integer, battery_id integer, battery_status integer);

copy battery_test
from '<s3 bucket object path>'
access_key_id '<access key here>'
secret_access_key '<secret key here>'
format as csv;

create table if not exists ml_output(battery_id integer not null, pass_fail decimal not null);

copy ml_output
from '<s3 bucket object path>'
access_key_id '<access key here>'
secret_access_key '<secret key here>'
format as csv;

create table if not exists spark_output(battery_id integer not null, fail_chance real not null,
sample_size integer);

copy ml_output
from '<s3 bucket object path>'
access_key_id '<access key here>'
secret_access_key '<secret key here>'
format as csv;

create table battery_status_legend(battery_status integer, name varchar);

copy battery_status_legend
from '<s3 bucket object path>'
access_key_id '<access key here>'
secret_access_key '<secret key here>'
format as csv;
