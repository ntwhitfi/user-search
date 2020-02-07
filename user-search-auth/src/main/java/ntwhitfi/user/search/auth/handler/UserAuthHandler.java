package ntwhitfi.user.search.auth.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import ntwhitfi.user.search.auth.config.DaggerUserAuthComponent;
import ntwhitfi.user.search.auth.config.UserAuthComponent;
import ntwhitfi.user.search.auth.model.AuthRequest;
import ntwhitfi.user.search.auth.model.AuthResponse;
import ntwhitfi.user.search.auth.service.IUserAuthService;

@Slf4j
public class UserAuthHandler implements RequestHandler<AuthRequest, AuthResponse> {
    private IUserAuthService authService;

    public UserAuthHandler(final UserAuthComponent userAuthComponent) {
        this(userAuthComponent.authService());
    }

    public UserAuthHandler(IUserAuthService userAuthService) {
        this.authService = userAuthService;
    }

    public UserAuthHandler() {
        this(DaggerUserAuthComponent.create());
    }

    @Override
    public AuthResponse handleRequest(AuthRequest authRequest, Context context) {
        //Check for a username and password. If either are empty, return a failed request response
        if(StringUtils.isNullOrEmpty(authRequest.getUsername()) || StringUtils.isNullOrEmpty(authRequest.getPassword())){
            return new AuthResponse(400, "Username and password are both required", null);
        }

        log.info("Attempting to authenticate user " + authRequest.getUsername());

        //Attempt to authenticate the user with the provided credentials
        return authService.login(authRequest.getUsername(), authRequest.getPassword());
    }
}
