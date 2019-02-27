## Redshift & OLAP Team

## S3
- To set up access to the S3 bucket:
  - Install the AWS CLI
  - Configure the S3 connection with $aws s3 configure
    - Provide the requested information, region as us-east-1 and default output format as Text
  - Should now be able to access the bucket with $aws s3 ls <bucket_name>
    - Some more commands that can be used with s3: https://docs.aws.amazon.com/cli/latest/reference/s3/

- Within S3:
  - Can copy files to and from S3 with $aws s3 cp <current_location> <target_location>
    - S3 locations must be formatted as 's3://bucket-name/path', local locations can be relative paths
  - Can create folders in the bucket implicitly:
    - When copying a file to s3 or moving a file within s3, add an extra step to the target location path
      to create a folder with that name i.e. s3://bucket-name/new-folder/file

- In addition to using AWS CLI, the S3 bucket can be accessed via S3 Browser

## RedShift
- To set up a connection to Redshift:
  - Any database tool compatible with Redshift is sufficient to connect to the database
  - When configuring a connection, select Redshift as the driver and input the endpoint, username, and password
    - Test the connection, if successful, everything should be ready

- List of Redshift commands: https://docs.aws.amazon.com/redshift/latest/dg/c_SQL_commands.html
  - Creating a table is essentially the same as Hive, with different data types, listed here:
    - https://docs.aws.amazon.com/redshift/latest/dg/c_Supported_data_types.html
  - Mainly use copy, which is Redshift's equivalent of Hive's load
    - Use access_key_id and secret_access_key fields as the authorization section of the copy statement

## Metabase
* Metabase is a free and open-source analytics tool that provides data visualization. 
* Metabase also provides various ways to share information to other users. 
* Metabase is only a tool for connecting to a database and visualizing data. It does not support its own data hosting.   

* Features:
    * Metabase is simple to set up (e.g. running Metabase locally only requires downloading and running a jar file provided on their web page).
    * A query builder that uses drop-down boxes, checkboxes, custom expressions, etc. to create "questions" that Metabase will answer. This feature is specially tailored for non-technical users. 
    * For more advanced users, Metabase provides an SQL scripting engine to ask questions that are out of the scope of its query builder.
    * A Slack bot and SMTP server capabilities are provided for automatic alerts (pulses).
    * Metabase is able to connect to most of the major RDBMS's, including AWS Redshift.

* Metabase was used for the following:
    * Querying the data within Redshift
    * Creating a dashboard to visualize the Spark Team's analysis
    * Configuring an email-based alert system

* For additional information about Metabase, refer to the markdown file in the Redshift-OLAP team folder called "metabase-detailed-guide."
