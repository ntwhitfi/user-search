package ntwhitfi.user.search.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;
import ntwhitfi.user.search.model.UserRecord;
import ntwhitfi.user.search.repository.IRepository;
import ntwhitfi.user.search.repository.UserRecordRepository;
import ntwhitfi.user.search.service.IUserSearchService;
import ntwhitfi.user.search.service.UserSearchService;

import javax.inject.Singleton;
import java.util.List;

@Module
public class UserSearchModule {

    @Provides
    @Singleton
    public AmazonDynamoDB dynamoDBClient() {
        return AmazonDynamoDBClientBuilder.standard().build();
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
