package ntwhitfi.user.search.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class UserSearchResponse {

    private int statusCode;
    private String statusMessage;
    private List<UserRecord> userRecords;
}
