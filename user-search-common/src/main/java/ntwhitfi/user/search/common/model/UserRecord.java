package ntwhitfi.user.search.common.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

@DynamoDBTable(tableName="UserSearchTable")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRecord {

    @DynamoDBHashKey(attributeName="id")
    private String id;

    @DynamoDBAttribute(attributeName="active")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
    private Boolean active;

    @DynamoDBAttribute(attributeName="firstName")
    private String firstName;

    @DynamoDBAttribute(attributeName = "lastName")
    private String lastName;

    @DynamoDBAttribute(attributeName = "department")
    private String department;

    @DynamoDBAttribute(attributeName = "searchTerm")
    private String searchTerm;
}
