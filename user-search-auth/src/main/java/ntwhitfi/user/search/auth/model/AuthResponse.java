package ntwhitfi.user.search.auth.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private int statusCode;
    private String statusMessage;
    private String authToken;
}
