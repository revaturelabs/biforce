![Biforce Brand](https://github.com/revaturelabs/biforce/blob/dev/images/biforce-brand.png)

## Description
Biforce is a project conducted by Revature to improve its business decisions via re-examination of existing metrics and investigation into new metrics that will increase value of company assets. The goal is to leverage all relevant technologies to automate the process of data analysis within the business intelligence life cycle conducted on different departments within the company. The objective is to implement efficient algorithms for data processing via tools available within the Hadoop ecosystem that will run on a physical and cloud cluster.

## Objectives
1. Collaborate on a two-week code sprint to create a prediction system using the Hadoop Ecosystem. 
2. Implement Apache Spark for processing, Sqoop for data migration, and Oozie for automation.
3. Start an AWS EMR cluster using a Spark Core instance to build the predictive model.
4. Build a multi-node physical cluster to process locally as a backup for the cloud cluster.
5. Store model results to Redshift.
6. Create Power BI interactive dashboard to display the results.  

## Current State
Trainee evaluations are gathered from [Caliber](https://github.com/revaturelabs/caliber) and transformed through the use of Sqoop commands. The transformation is automated with an Oozie workflow. The resulting output is used by traditional and machine learning algorithms written using Apache Spark, which are run inside both a physical and cloud cluster. Output from Spark is stored in an Amazon S3 bucket and then is imported by Amazon Redshift. Microsoft Power BI will then visualize the data stored in Redshift using dashboards and reports, which can distributed to various clients and stakeholders.

## Scope
The scope was limited to just the four types of tests and quality control notes from the Caliber Database.  

## Features
- Fully automated ETL workflow to transform and clean data from Caliber.
-  Traditional and Machine Learning Spark algorithms running on EMR and physical clusters.
- Power Bi Dashboard to display results that are stored in Redshift.  

## Stakeholders

### Primary Users

VP of Technology, Managers, Trainers

### Key Stakeholders

Management, Trainers 

## Strategic Alignment
- Strong base for future iterations with extensive documentation.
- Machine learning to increase.


 
