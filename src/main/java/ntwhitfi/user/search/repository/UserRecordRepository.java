package ntwhitfi.user.search.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.AllArgsConstructor;
import ntwhitfi.user.search.model.UserRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRecordRepository implements IRepository<List<UserRecord>>{
    private DynamoDBMapper dynamoDBMapper;

    public UserRecordRepository(final DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public List<UserRecord> getRecords(String query) {
        //Search DynamoDB table for records matching the given query
        Map<String, AttributeValue> attributeValueMap = new HashMap<String, AttributeValue>();
        attributeValueMap.put(":active", new AttributeValue().withBOOL(true));
        attributeValueMap.put(":firstName", new AttributeValue().withS(query));
        attributeValueMap.put(":lastName", new AttributeValue().withS(query));
        attributeValueMap.put(":department", new AttributeValue().withS(query));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("active = :active and (contains (firstName, :firstName) OR contains (lastName, :lastName) OR department = :department)")
                .withExpressionAttributeValues(attributeValueMap);

        List<UserRecord> userRecordList = dynamoDBMapper.scan(UserRecord.class, scanExpression);

        return userRecordList;
    }

    public List<UserRecord> getAllRecords() {
        //Return all the active user records from the DynamoDB table
        Map<String, AttributeValue> attributeValueMap = new HashMap<String, AttributeValue>();
        attributeValueMap.put(":active", new AttributeValue().withBOOL(true));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("active = :active").withExpressionAttributeValues(attributeValueMap);

        List<UserRecord> userRecordList = dynamoDBMapper.scan(UserRecord.class, scanExpression);

        return userRecordList;
    }
}
