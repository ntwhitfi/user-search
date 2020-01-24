package ntwhitfi.user.search.etl.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;
import ntwhitfi.user.search.common.model.UserRecord;
import ntwhitfi.user.search.common.repository.IRepository;
import ntwhitfi.user.search.common.repository.UserRecordRepository;
import ntwhitfi.user.search.etl.IUserETLService;
import ntwhitfi.user.search.etl.UserETLService;
import ntwhitfi.user.search.etl.model.HRUserRecord;
import ntwhitfi.user.search.etl.repository.HRUserRecordRepository;

import javax.inject.Singleton;
import java.util.List;

@Module
public class UserETLModule {

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
    public IRepository<List<HRUserRecord>> hrUserRecordRepository(final DynamoDBMapper dynamoDBMapper) {
        return new HRUserRecordRepository(dynamoDBMapper);
    }

    @Provides
    @Singleton
    public IUserETLService userETLService(final IRepository<List<HRUserRecord>> hrUserRecordRepository, IRepository<List<UserRecord>> userRecordRepository) {
        return new UserETLService(hrUserRecordRepository, userRecordRepository);
    }
}
