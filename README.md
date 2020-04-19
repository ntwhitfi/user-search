# user-search
Serverless user search application

The user search application is a sample app used to display some serverless patters. 

## user-search-common
Contains common classes for the user search application

## user-search-etl
This module is responsible for pulling records from the HRUserTable, transforming these records, and pushing them into the UserSearchTable.

## user-search-service
This module is responsible for searching the UserSearchTable and returning user details. 

## user-search-ui
This module contains the UI for the user search application. This UI is hosted in AWS S3 and served via AWS CloudFront

## Manual Deployment Steps
The CloudFront Distributions for the UI, the Custom Domain Names used for the APIs and the required DNS records, SSL Certificates, and Route53
hosted zone setup are managed manually in the AWS account at this time.