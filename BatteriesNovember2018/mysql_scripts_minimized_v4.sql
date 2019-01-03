--
---- creating tables in mysql
create table battery_group_relationship (battery_group decimal(10, 0) not null, battery_id decimal(10, 0) not null);
create table battery_test (battery_test_id decimal(19, 0) not null, raw_score decimal(10, 0) not null, battery_test_type varchar(255) not null, test_period decimal(5,0) not null, battery_group decimal(10, 0) not null, test_category decimal(10, 0) not null);
create table battery_test_grade (voltage_id decimal(19, 0) not null, date_received timestamp(6) not null, score decimal(5,2) not null, battery_test_id decimal(19, 0) not null, battery_id decimal(10, 0) not null);
create table battery_note (note_id decimal(7, 0) not null, max_visibility decimal (3, 0) not null, is_AAA_feedback decimal (1, 0) not null, AAA_status varchar(25), note_type varchar(25) not null, test_period decimal (4, 0) not null, battery_group decimal (10, 0) not null, battery_id decimal (6, 0) not null);
create table battery_status (battery_id decimal (8, 0) not null, battery_status varchar(15) not null);


--
---- initial load
mysqlimport --fields-terminated-by , --verbose --local -u root -p Battery_Staging <path to battery_group_relationship.csv>
mysqlimport --fields-terminated-by , --verbose --local -u root -p Battery_Staging <path to battery_test.csv>
mysqlimport --fields-terminated-by , --verbose --local -u root -p Battery_Staging <path to battery_test_grade.csv>

update battery_note set AAA_status = replace(AAA_status,'"','');
update battery_note set note_type = replace(note_type,'"','');
mysqlimport --fields-terminated-by , --verbose --local -u root -p Battery_Staging <path to battery_note.csv>

update battery_status set battery_status = replace(battery_status,'"','');
mysqlimport --fields-terminated-by , --verbose --local -u root -p Battery_Staging <path to battery_status.csv>


--
---- sqoop import
sqoop import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table battery_group_relationship -m 1
sqoop import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table battery_test -m 1
sqoop import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table battery_test_grade -m 1
sqoop import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table battery_note -m 1
sqoop import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table battery_status -m 1


--
---- incremental load via scoop
sqoop job --create NoteIncrementalView -- import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table _view_battery_note --incremental append --check-column note_id --last-value 381046 --target-dir <HDFS path to _view_battery_note> -m 1
sqoop job --create PerformanceIncrementalView -- import --connect jdbc:mysql://localhost/Battery_Staging --username root --password cloudera --table _view_battery_performance --incremental append --check-column date_received --last-value 2018-03-16 01:39:37 --target-dir <HDFS path to _view_battery_performance> -m 1


-- last values
---- _view_battery_note
381046
---- _view_battery_performance
2018-03-16 01:39:37
