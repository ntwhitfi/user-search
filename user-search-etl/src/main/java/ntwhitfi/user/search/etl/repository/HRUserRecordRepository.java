package ntwhitfi.user.search.etl.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.extern.slf4j.Slf4j;
import ntwhitfi.user.search.common.model.UserRecord;
import ntwhitfi.user.search.common.repository.IRepository;
import ntwhitfi.user.search.etl.model.HRUserRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HRUserRecordRepository implements IRepository<List<HRUserRecord>> {
    private DynamoDBMapper dynamoDBMapper;

    public HRUserRecordRepository(final DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public List<HRUserRecord> getRecords(String query) {
        log.info("Method not implemented");
        return null;
    }

    public List<HRUserRecord> getAllRecords() {
        //retrieve all records from the dynamodb table
        return dynamoDBMapper.scan(HRUserRecord.class, new DynamoDBScanExpression());
    }

    @Override
    public void pushRecords(List<HRUserRecord> records) {
        log.info("Method not implemented");
    }
}
