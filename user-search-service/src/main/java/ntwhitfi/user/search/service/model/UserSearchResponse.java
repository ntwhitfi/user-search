package ntwhitfi.user.search.service.model;

import lombok.*;
import ntwhitfi.user.search.common.model.UserRecord;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchResponse {

    private int statusCode;
    private String statusMessage;
    private List<UserRecord> userRecords;
}
