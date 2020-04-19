import boto3

dynamodb_client = boto3.client('dynamodb')

# Attempt to create the global UserSearchTable
try:
    response = dynamodb_client.create_global_table(
        GlobalTableName='UserSearchTable',
        ReplicationGroup=[
            {
                'RegionName': 'us-east-1'
            },
            {
                'RegionName': 'us-west-1'
            },
        ]
    )
except dynamodb_client.exceptions.ResourceInUseException:
    pass # do nothing, table exists