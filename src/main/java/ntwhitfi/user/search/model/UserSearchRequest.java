package ntwhitfi.user.search.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchRequest {

    private String type;
    private String query;
}
