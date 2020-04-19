package ntwhitfi.quarkus.user.search.service.model;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class HealthResponse {
    private int statusCode;
    private String statusMessage;
}
