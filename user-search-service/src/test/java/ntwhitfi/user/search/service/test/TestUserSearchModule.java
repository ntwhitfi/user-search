package ntwhitfi.user.search.service.test;

import cloud.localstack.TestUtils;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;
import ntwhitfi.user.search.common.model.UserRecord;
import ntwhitfi.user.search.common.repository.IRepository;
import ntwhitfi.user.search.common.repository.UserRecordRepository;
import ntwhitfi.user.search.service.IUserSearchService;
import ntwhitfi.user.search.service.UserSearchService;

import javax.inject.Singleton;
import java.util.List;

@Module
public class TestUserSearchModule {

    @Singleton
    @Provides
    public AmazonDynamoDB amazonDynamoDB() {
        return TestUtils.getClientDynamoDB();
    }

    @Provides
    @Singleton
    public DynamoDBMapper dynamoDBMapper(final AmazonDynamoDB dynamoDBClient) {
        return new DynamoDBMapper(dynamoDBClient);
    }

    @Provides
    @Singleton
    public IRepository<List<UserRecord>> userRecordRepository(final DynamoDBMapper dynamoDBMapper) {
        return new UserRecordRepository(dynamoDBMapper);
    }

    @Provides
    @Singleton
    public IUserSearchService userSearchService(final IRepository<List<UserRecord>> userRecordRepository) {
        return new UserSearchService(userRecordRepository);
    }
}
