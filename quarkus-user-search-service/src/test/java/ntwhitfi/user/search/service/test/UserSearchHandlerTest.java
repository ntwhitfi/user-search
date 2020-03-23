package ntwhitfi.user.search.service.test;

import cloud.localstack.LocalstackTestRunner;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import ntwhitfi.user.search.common.model.UserRecord;
import ntwhitfi.user.search.common.repository.IRepository;
import ntwhitfi.quarkus.user.search.service.handler.UserSearchHandler;
import ntwhitfi.quarkus.user.search.service.model.UserSearchRequest;
import ntwhitfi.quarkus.user.search.service.model.UserSearchResponse;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ System.class, TestUserSearchModule.class, LocalstackTestRunner.class })
@PowerMockRunnerDelegate(LocalstackTestRunner.class)
@PowerMockIgnore(value = { "javax.management.*", "org.apache.http.*", "javax.net.ssl.*", "javax.crypto.*" })
@LocalstackDockerProperties(randomizePorts = true, services = { "dynamodb" })
public class UserSearchHandlerTest {

    private AmazonDynamoDB amazonDynamoDB;
    private TestUserSearchModule userSearchModule;
    private UserSearchHandler userSearchHandler;
    private IRepository<List<UserRecord>> userRecordRepository;
    private DynamoDBMapper dynamoDBMapper;

    // Create test record to add
    private UserRecord testUserRecord = UserRecord.builder()
            .active(true)
            .id("test-record-id")
            .department("IT")
            .firstName("Nathan")
            .lastName("Whitfield")
            .searchTerm("nathan_whitfield_it")
            .build();

    @Before
    public void setUp() throws Exception {
        // Create new dagger module with dependent resources
        userSearchModule = new TestUserSearchModule();

        // Setup amazon dynamodb client for locastack and create the tables required for the user search handler tests and add a new record to search
        amazonDynamoDB = userSearchModule.amazonDynamoDB();
        createTestTable();

        // Create the userRecordRepository and put a test record
        dynamoDBMapper = userSearchModule.dynamoDBMapper(amazonDynamoDB);
        userRecordRepository = userSearchModule.userRecordRepository(dynamoDBMapper);
        putTestRecord();

        // Create the user search handler to test
        userSearchHandler = new UserSearchHandler(userSearchModule.userSearchService(userRecordRepository));

        // Set default return values for configured environment variables for the lambda function
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.getenv("QUERY_TYPE")).thenReturn("query");
    }

    // Test the user search handler to pull the created test record.
    @Test
    public void testUserRecordSearch() {
        // Given
        UserSearchResponse userSearchResponse;
        UserSearchRequest userSearchRequest = UserSearchRequest.builder().query("Nathan").type("query").build();

        // When
        userSearchResponse = userSearchHandler.handleRequest(userSearchRequest, null);

        // Then
        assertThat(userSearchResponse).isNotNull();
        assertThat(userSearchResponse.getStatusCode()).isEqualTo(200);
        assertThat(userSearchResponse.getUserRecords().get(0).getId()).isEqualTo(testUserRecord.getId());
    }

    // Create the test DynamoDB Table
    private void createTestTable() {
        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName("UserSearchTable")
                .withProvisionedThroughput((new ProvisionedThroughput()).withReadCapacityUnits(5L).withWriteCapacityUnits(6L))
                .withKeySchema(new KeySchemaElement[]{new KeySchemaElement("id", KeyType.HASH)})
                .withAttributeDefinitions(attributeDefs("id"));

        amazonDynamoDB.createTable(createTableRequest);
    }

    // Put a test record in the created DynamoDB Table
    private void putTestRecord() {
        List<UserRecord> testRecords = Lists.newArrayList(testUserRecord);

        userRecordRepository.pushRecords(testRecords);
    }

    // Create the attribute definitions for the create table request
    private List<AttributeDefinition> attributeDefs(String keyAttributeName) {
        List<AttributeDefinition> defs = Lists.newArrayList();
        defs.add((new AttributeDefinition()).withAttributeName(keyAttributeName).withAttributeType("S"));
        return defs;
    }
}
