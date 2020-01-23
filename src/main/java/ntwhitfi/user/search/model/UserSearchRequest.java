package ntwhitfi.user.search.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSearchRequest {

    private String type;
    private String query;
}
