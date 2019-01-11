## Redshift & OLAP Team

## S3:
- To set up access to the S3 bucket:
  - Install the AWS CLI
  - Configure the S3 conenction with $aws s3 configure
    - Provide the requested information, region as us-east-1 and default output format as Text
  - Should now be able to access the bucket with $aws s3 ls <bucket_name>
    - Some more commands that can be used with s3: https://docs.aws.amazon.com/cli/latest/reference/s3/

- Within S3:
  - Can copy files to and from S3 with $aws s3 cp <current_location> <target_location>
    - S3 locations must be formatted as 's3://bucket-name/path', local locations can be relative paths
  - Can create folders in the bucket implicitly:
    - When copying a file to s3 or moving a file within s3, add an extra step to the target location path
      to create a folder with that name i.e. s3://bucket-name/new-folder/file

## RedShift:
- To set up a connection to RedShift:
  - Install SQL Workbench (SQL Developer has compatibility issues with the required jar)
  - Download the RedshiftJDBC42-1.2.1.1001 jar online (or the version of the jar that your version of SQL Workbench needs)
  - When configuring a connection, select RedsShift as the driver, and input the endpoint, username, and password
    - Test the connection, if successful, everything should be ready

- List of RedShift commands: https://docs.aws.amazon.com/redshift/latest/dg/c_SQL_commands.html
  - Creating a table is essentially the same as Hive, with different data types, listed here:
    - https://docs.aws.amazon.com/redshift/latest/dg/c_Supported_data_types.html
  - Mainly use copy, which is RedShift's equivalent of Hive's load
    - Use access_key_id and secret_access_key fields as the authorization section of the copy statement
      - There is probably a better, more secure way to do this, we had issues figuring out how.
    - Make sure, after the access key and secret key sections of the copy statement, use 'format as csv' to avoid
      running into "Delimiter not found" errors.

## Microsoft Power BI
- Power BI is a business analytic solution that allows visualize data and share information with the hole organization or
publish it to web. Powerfull tool used to manage large amounts of records with the ability to connect to almost
every database or data storage.
- It provides cloud based services along with a desktop interface that you can use to design your dashboard and
reports to then share them with your organization. It offers data warehouse capabilities including data preparaation,
data discovery and interactive dashboards.

- This business intelligence tool was used as a OLAP tool to present the findings of your processing.
- It was used to:
  - Connect to Redshift.
  - Create reports with findings.
  - Design interactive dashboards for better visualization of the data.

## Connect to Redshift
  - Go to "Home" section of your tab.
  - Click "Get Data" option.
  - Select "More..." option.
  - Choose "Database" at the left part of the window and click Amazon Redshift.
  - A window will open for you to enter your server url and the name of the database and click "Ok".
  - Write your credentials username and password and click "Connect".
  - Select your tables and click "Load".
  - DONE!

 ## Create and Design Reports
  - Look for resources like powerbi.com, youtube tutorials, and web tutorials.
  - One really good tutorial:
    - https://www.youtube.com/watch?v=1bysgMsPwC4&list=PL7GQQXV5Z8ef2SjkDpLnvsz7TAQjlzlpO
