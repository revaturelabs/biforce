create table battery_test_relationship (battery_group decimal(10, 0) not null, battery_id decimal(10, 0) not null);
create table battery_test (battery_test_id decimal(19, 0) not null, raw_score decimal(10, 0) not null, battery_test_type varchar(255) not null, test_period decimal(5,0) not null, battery_group decimal(10, 0) not null, test_category decimal(10, 0) not null);
create table battery_test_grade (voltage_id decimal(19, 0) not null, date_received timestamp(6) not null, score float not null, battery_test_id decimal(19, 0) not null, battery_id decimal(10, 0) not null);

sqoop export --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera  --table battery_group_relationship  --export-dir BATTERY_GROUP_RELATIONSHIP
sqoop export --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera  --table battery_test  --export-dir BATTERY_TEST
sqoop export --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera  --table battery_test_grade  --export-dir BATTERY_TEST_GRADE
-- sqoop import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table battery_group_relationship

---- initial load
mysqlimport --fields-terminated-by , --verbose --local -u root -p Battery_Staging battery_group_relationship.csv
mysqlimport --fields-terminated-by , --verbose --local -u root -p Battery_Staging battery_test.csv
mysqlimport --fields-terminated-by , --verbose --local -u root -p Battery_Staging battery_test_grade.csv


---- incremental load via scoop
sqoop job --create IncrementalView -- import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table view_Every_Raw_Score --incremental append --check-column battery_group --last-value 116502 --target-dir view_Every_Raw_Score -m 1

---- creating the view
create view view_Every_Raw_Score as select battery_test_grade.battery_id, battery_test.battery_group, battery_test.battery_test_type, battery_test.test_period, battery_test_grade.score, battery_test.test_category from battery_test_grade left join battery_test on battery_test_grade.battery_test_id = battery_test.battery_test_id;