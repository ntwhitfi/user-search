#!/bin/bash

echo building $ENVIRONMENT environment
npm install
npm run build

aws s3 sync --delete --acl bucket-owner-full-control dist/ "s3://${BUCKET_NAME}"
aws configure set preview.cloudfront true

aws cloudfront create-invalidation --distribution-id ${CLOUDFRONT_DISTRIBUTION_ID} --paths '/*'
echo Build completed on `date`
