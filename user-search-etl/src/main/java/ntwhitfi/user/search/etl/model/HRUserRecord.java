package ntwhitfi.user.search.etl.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

@DynamoDBTable(tableName="HRUserTable")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HRUserRecord {

    @DynamoDBHashKey(attributeName="id")
    private String id;

    @DynamoDBAttribute(attributeName="terminationDate")
    private String terminationDate;

    @DynamoDBAttribute(attributeName="firstName")
    private String firstName;

    @DynamoDBAttribute(attributeName = "lastName")
    private String lastName;

    @DynamoDBAttribute(attributeName = "empId")
    private String empId;

    @DynamoDBAttribute(attributeName = "userId")
    private String userId;

    @DynamoDBAttribute(attributeName = "role")
    private String role;

}
