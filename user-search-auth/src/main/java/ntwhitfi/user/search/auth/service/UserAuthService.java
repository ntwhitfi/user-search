package ntwhitfi.user.search.auth.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import ntwhitfi.user.search.auth.model.AuthResponse;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserAuthService implements IUserAuthService {
    //The ID that references the Cognito User Pool
    private static String USER_POOL_ID = System.getenv("USER_POOL_ID");
    //Client ID to use for making calls to Cognito
    private static String CLIENT_ID = System.getenv("CLIENT_ID");

    private AWSCognitoIdentityProvider cognitoIdentityClient;

    public UserAuthService(AWSCognitoIdentityProvider cognitoIdentityClient) {
        this.cognitoIdentityClient = cognitoIdentityClient;
    }

    public AuthResponse login(String username, String password) {
        //Create auth parameters for the Cognito login request
        Map<String, String> authParams = new HashMap<String, String>();
        authParams.put("USERNAME", username);
        authParams.put("PASSWORD", password);

        //Create the request for the Cognito login
        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthParameters(authParams)
                .withUserPoolId(USER_POOL_ID)
                .withClientId(CLIENT_ID);

        try {
            AdminInitiateAuthResult authResult = cognitoIdentityClient.adminInitiateAuth(authRequest);

            //check if there were any challenges set in the auth result
            if(StringUtils.isNullOrEmpty(authResult.getChallengeName())) {
                AuthenticationResultType authResultType = authResult.getAuthenticationResult();

                //return the auth token if it was received
                if(StringUtils.isNullOrEmpty(authResultType.getAccessToken())) {
                    log.error("Unable to authenticate user " + username);
                    return new AuthResponse(401, "Failed to authenticate user " + username, null);
                } else {
                    log.info("Successfully authenticated user " + username);
                    return new AuthResponse(200, "Success", authResultType.getAccessToken());
                }
            } else {
                //Auth challenge failed. Log the challenge failure and return an unauthenticated response
                log.error(String.format("Unable to authenticate user %s. Auth challenge received: %s", username, authResult.getChallengeName()));
                return new AuthResponse(401, String.format("Unable to authenticate user %s. Auth challenge received: %s", username, authResult.getChallengeName()), null);
            }
        } catch (Exception ex) {
            log.error(String.format("Unable to authenticate user %s. Exception: %s", username, ex));
            return new AuthResponse(401, String.format("Unable to authenticate user %s. Exception: %s", username, ex), null);
        }
    }
}
