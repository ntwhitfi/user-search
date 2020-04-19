#!/bin/bash

echo creating tables for $ENVIRONMENT environment


aws dynamodb create-table --table-name UserSearchTable \
    --attribute-definitions AttributeName=id,AttributeType=S --key-schema AttributeName=id,KeyType=HASH \
    --billing-mode PAY_PER_REQUEST \
    --stream-specification StreamEnabled=true,StreamViewType=NEW_AND_OLD_IMAGES \
    --region us-east-1

aws dynamodb wait table-exists --table-name UserSearchTable

aws dynamodb update-table --table-name UserSearchTable --cli-input-json  \
    '{
      "ReplicaUpdates":
      [
        {
          "Create": {
            "RegionName": "us-west-1"
          }
        }
      ]
    }'