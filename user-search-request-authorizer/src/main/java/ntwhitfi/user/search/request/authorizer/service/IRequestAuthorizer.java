package ntwhitfi.user.search.request.authorizer.service;

import ntwhitfi.user.search.request.authorizer.model.AuthResponse;

public interface IRequestAuthorizer {

    AuthResponse validateAuthToken(String authToken);
    String getJWTPrincipalId(String jwt);
}
