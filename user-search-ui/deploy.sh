#!/bin/bash

aws s3 sync --delete --acl bucket-owner-full-control dist/ "s3://${BUCKET_NAME}" --region ${REGION}
aws configure set preview.cloudfront true --region ${REGION}

aws cloudfront create-invalidation --distribution-id ${CLOUDFRONT_DISTRIBUTION_ID} --paths '/*' --region ${REGION}
echo Build completed on `date`
