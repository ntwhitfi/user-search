package ntwhitfi.quarkus.user.search.service.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import ntwhitfi.quarkus.user.search.service.model.HealthResponse;

public class UserSearchHealthHandler implements RequestHandler<Object, HealthResponse> {

    @Override
    public HealthResponse handleRequest(Object o, Context context) {
        // Return health reponse
        return HealthResponse.builder().statusCode(200).statusMessage("UP").build();
    }
}
