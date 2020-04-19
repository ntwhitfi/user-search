import boto3

dynamodb_client = boto3.client('dynamodb', region_name='us-east-1')

# Attempt to create the global UserSearchTable
try:
    response = dynamodb_client.create_table(
        TableName='UserSearchTable',
        AttributeDefinitions=[
            {
                'AttributeName': 'id',
                'AttributeType': 'S'
            }
        ],
        KeySchema=[
            {
                'AttributeName': 'id',
                'KeyType': 'HASH'
            }
        ],
        BillingMode='PAY_PER_REQUEST',
        StreamSpecification={
            'StreamEnabled': True,
            'StreamViewType': 'NEW_AND_OLD_IMAGES'
        }
    )
except dynamodb_client.exceptions.ResourceInUseException:
    pass # do nothing, table exists

dynamodb_client = boto3.client('dynamodb', region_name='us-west-1')

# Attempt to create the global UserSearchTable
try:
    response = dynamodb_client.create_table(
        TableName='UserSearchTable',
        AttributeDefinitions=[
            {
                'AttributeName': 'id',
                'AttributeType': 'S'
            }
        ],
        KeySchema=[
            {
                'AttributeName': 'id',
                'KeyType': 'HASH'
            }
        ],
        BillingMode='PAY_PER_REQUEST',
        StreamSpecification={
            'StreamEnabled': True,
            'StreamViewType': 'NEW_AND_OLD_IMAGES'
        }
    )
except dynamodb_client.exceptions.ResourceInUseException:
    pass # do nothing, table exists

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
