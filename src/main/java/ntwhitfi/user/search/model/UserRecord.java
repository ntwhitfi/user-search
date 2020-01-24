package ntwhitfi.user.search.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
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
    private Boolean active;

    @DynamoDBAttribute(attributeName="firstName")
    private String firstName;

    @DynamoDBAttribute(attributeName = "lastName")
    private String lastName;

    @DynamoDBAttribute(attributeName = "department")
    private String department;

}
