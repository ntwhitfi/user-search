package ntwhitfi.user.search.etl.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserETLResponse {
    private int statusCode;
    private String statusMessage;
}
