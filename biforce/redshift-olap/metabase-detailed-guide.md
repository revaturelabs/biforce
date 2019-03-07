# How to Get and Use Metabase
## Installation
### Local Machine
[Follow this link from the Metabase documentation](https://metabase.com/docs/latest/operations-guide/running-the-metabase-jar-file.html)

### Elastic Beanstalk
[Follow this link from the Metabase documentation](https://metabase.com/docs/latest/operations-guide/running-metabase-on-elastic-beanstalk.html)

## Setting up the Portal
[Follow this link from the Metabase documentation](https://metabase.com/docs/latest/setting-up-metabase.html)

## Navigating the Metabase Home Screen
* On the home screen, the following information is provided to the user:
    * Automatically generated quick summaries of data tables called "x-rays" (this can be disabled in admin mode; see admin mode section for more information)
    * A link to "Our Analytics," the part of the portal that summarizes all collections, dashboards, questions, and pulses. Each of these items will be discussed altogether in the section on "Our Analytics"
    * A link to "Our Data," the part of the portal that provides quick access to descriptions on the databases within the portal and those databases' tables. "Our Data" is where the user can tell Metabase to x-ray additional tables besides those it already displays on the home page. "Our Data" will be discussed in further detail in its own section
* The top of all web pages within the portal will have the following selections:
    * The Metabase logo for navigation back to the portal's home screen
    * A search bar
    * "Ask a question," which sends the user to Metabase's query builder (discussed in further detail in its own section)
    * The plus symbol, which shows the links to the dashboard and pulse creation screens
    * The square symbol, which sends the user to the data reference screen. This screen lists information on all databases, tables, metrics, and segments. Information such as database and table descriptions must be populated by the user by clicking on "Edit." Metabase automatically catalogs table column information, schema information, and saved questions
    * Clicking on the bell symbol sends the user to Metabase's log of all user activity and Metabase's suggested setup items
    * Clicking on the gear symbol shows the following options:
        * Account Settings: Change user name and password information here
        * Admin: Switches the user portal to admin mode (see the section on admin mode for more information)
        * Logs: Shows the logs in a snapshot of the terminal
        * Help: Redirects the user to Metabase's documentation
        * About Metabase: Shows the current version of Metabase
        * Sign Out: Logs the user out of the portal

## Questions, Dashboards, and Pulses
### Questions in Metabase
* The word question within Metabase is synonymous with the word query within SQL
* Metabase presents querying a database using the non-technical terminology "ask a question"
* The query builder in Metabase is accessed by clicking on "ask a question" at the top of any portal web page
* The main screen of the query builder presents three options to the user:

    a. Metrics: Navigation to any user-defined metrics. see the section on metrics and segments for more information
    
    b. Custom: This is Metabase's query builder for non-technical users. Use this interface for simple select statements such as filters, sorts, and groups. All queries created in this builder can be saved for future use (they become "saved questions"). Note that the SQL code used to create a question can be viewed at any time by clicking "switch to SQL." 
    
    c. Native Query: This is for users with an SQL background. Complex queries such as nested queries and joins can only be performed in Native Query. Saved questions can be created here as well.

* The output of a question in Metabase may be a graph or a table, depending on the display option selected in the "Custom" query builder. Supported graph types are line graphs, bar graphs, pie charts, scatter plots, and area graphs. There are several other visualization options such as gauges and maps. Metabase will gray out those visualization options that are not supported for the answer to the user's question.

### The Dashboard
* In Metabase, the dashboard is a group of saved questions housed by default in the user's "Our Analytics" page.
* Creation of a dashboard is done by hitting the plus symbol at the top of any webpage and selecting "New dashboard"
* All dashboards have a title, a short description, and a location. This location is called a collection.
* A dashboard's main page houses all the displays from the saved questions that have been added to it. Each saved question is a tile on the dashboard.
* Options provided on a dashboard's main screen:

    a. Add a question: Create a new question tile for the dashboard

    b. Edit dashboard: Modify questions, move questions to different parts of the dashboard, edit the title and description of the dashboard

    c. Move dashboard: Place the dashboard in a different collection

    d. Duplicate dashboard: Create a copy of the dashboard

    e. Auto-Refresh: Configure automatic syncing of the information presented on the dashboard with the contents of the associated database

    f. Enter full screen mode

### The Pulse
* Pulses are the fundamental units of Metabase's alert system
* Creation of a pulse is done by hitting the plus symbol at the top of any webpage and selecting "New pulse"
* A pulse consists of a name, a collection location, a set of saved questions, and a notification system(s) (email and/or Slack)
* Email and/or Slack must first be set up in Admin mode before the use of pulses is allowed
* Note that if Slack and/or email are not set up yet, the pulse creation screen will provide buttons that if clicked will redirect the user to the email or Slack configuration screen
* An optional setting for pulses is sending them at regular intervals  

## Metrics and Segments
### Metrics
* Metrics are user-defined aggregate functions that after their creation are made available for use in the custom query builder
* All metrics can only be used within the data tables they are created for
* To create a metric, switch to admin mode and then click on "Data Model"
* In the "Data Model" screen, click on any data table
* Along with the column information will be any previously created metrics
* Find the option called "Add a metric" to reach the metric creation screen
* In short, a metric is an aggregate function created via Metabase's query builder that can be written once, but applied many times to future saved questions

### Segments
* Segments are custom filters on data tables that after their creation are made available for use in the custom query builder
* All segments can only be used within the data tables they are created for
* To create a segment, switch to admin mode and then click on "Data Model"
* In the "Data Model" screen, click on any data table
* Along with the column information will be any previously created segments
* Find the option called "Add a segment" to reach the segment creation screen
* In short, a segment is an SQL select statement made in Metabase's query builder that can be written once, but applied many times in future saved questions

### Metabase Documentation Page on Metrics and Segments
[Follow this link for a more detailed guide to metrics and segments](https://www.metabase.com/docs/latest/administration-guide/07-segments-and-metrics.html)

## Additional Info on Basic Metabase Usage
[Follow this link to Metabase's getting started guide](https://metabase.com/docs/latest/getting-started.html)

## Overview of Admin Mode Features
### Settings Tab
1. Setup:
    * Shows a to-do list of items that Metabase recommends that the admin configure
    * Within this list are setting up email, adding databases, integrating Slack, inviting new users, hiding unused tables, organizing queries (called "questions" by Metabase), creating metrics (explained in previous sections), and creating segments (explained in previous sections)
2. General:
    * This section allows the admin to update the site name, the site URL, the email address for help requests, the site's time zone, and the site's language
    * The remaining items in this section are toggles that enable/disable tracking by Metabase, usage of "friendly" table/field names, nested queries, and x-ray features (covered in later sections)
3. Updates:
    * Toggles whether or not Metabase should automatically check for software updates
4. Email:
    * This is where SMTP server integration is configured so that Metabase can send automatically generated emails
5. Slack:
    * This is where the admin can create a Slack bot (called MetaBot) for their portal and after creation configure it.
    * MetaBot handles pulses created for delivery via Slack
6. Authentication:
    * Configure sign-in with Google
    * Configure LDAP, if relevant
7. Map:
    * Use this screen to add a map to Metabase
8. Formatting:
    * This section is for setting the date format, the time format, the number separator format, and the currency format
9. Public Sharing:
    * Use this for toggling the ability for users to create links for dashboards and saved questions (dashboards are covered in previous sections)
10. Embedding in other Applications:
    * Enables/Disables embedding dashboards and other Metabase portal items into external applications
11. Caching:
    * Enables/Disables caching
### People Tab
* View all users and groups associated with the Metabase portal
* The admin can add new users, update user information, perform password resets, create new user groups, and provide admin privileges to users from this screen
### Data Model Tab
* View the schema of each database in the portal
* Make changes to a database's schema
* Create metrics and segments for a data table
### Databases Tab
* View all databases in the Metabase portal
* This is where new databases can be added, existing databases can be deleted, and where to update database information

    Manual Sync and Manual Field Scan:
    
    One of the most important parts of this section is found after clicking on the name of a database. On the right are two options under "Actions": 1) Sync database schema now and 2) Re-scan field values now. Use these actions to ensure that any DML and alter statements performed on the database outside of Metabase are reflected in the portal.
### Permissions Tab
* Manages which user groups can perform data accesses and SQL queries on databases in the portal
### Troubleshooting Tab
* Displays Metabase's logs
### Leaving Admin Mode
* Click on the gear symbol in the top right of the web page
* This brings up a settings menu
* The second option from the top is "Exit Admin." This will switch the Metabase portal back to user mode
### Additional Info on Admin Mode
[Follow this link for more info on admin mode](https://metabase.com/docs/latest/administration-guide/start.html)

## References
https://metabase.com/docs/latest/
