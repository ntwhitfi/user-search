package ntwhitfi.user.search.request.authorizer.service;

import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import lombok.extern.slf4j.Slf4j;
import ntwhitfi.user.search.request.authorizer.config.Constants;
import ntwhitfi.user.search.request.authorizer.model.AuthResponse;
import ntwhitfi.user.search.request.authorizer.util.AuthTokenProcessor;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SuppressWarnings("unchecked")//Prevent throwing warning on build due to the fromJson call converting an unspecified HashMap
public class RequestAuthorizer implements IRequestAuthorizer{
    //Constants for JWT info. Split pattern, index of the JWT payload and number of parts expected from the split JWT
    private static String JWT_SPLIT_PATTERN = "\\.";
    private static int JWT_PAYLOAD_INDEX = 1;
    private static int JWT_PARTS_COUNT = 3;
    private static String COGNITO_USERNAME_PAYLOAD_KEY = "username";
    private static String JWT_ISSUER_KEY = "iss";

    @Override
    public AuthResponse validateAuthToken(String authToken) {
        if(isValidJWT(authToken)) {
            //send successful auth to response for valid tokens
            log.info("Successfully validated auth token");
            return AuthResponse.builder().statusCode(200).statusMessage("Auth token validated successfully").build();
        } else {
            log.error("Failed to validate auth token");
            //send forbidden response for invalid auth tokens
            return AuthResponse.builder().statusCode(403).statusMessage("Forbidden").build();
        }
    }

    @Override
    public String getJWTPrincipalId(String jwt) {
        log.debug("JWT to retrieve principal id for: " + jwt);
        Map<String, String> jwtPayloadMap = new Gson().fromJson(getJWTPayload(jwt), HashMap.class);

        //return the principal id (in this case our username) from the token or return null if it is not found
        return jwtPayloadMap.getOrDefault(COGNITO_USERNAME_PAYLOAD_KEY, null);
    }

    private boolean isValidJWT(String jwt) {
        //String[] jwtParts = jwt.split(JWT_SPLIT_PATTERN);
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = AuthTokenProcessor.getJWTProcessor();

        if(jwtProcessor != null) {
            //Process the JWT token and check the claims returned. if the token was processed correctly
            try {
                JWTClaimsSet claimsSet = jwtProcessor.process(jwt, null);
                log.debug("JWT Claims Set " + new Gson().toJson(claimsSet.getClaims()));
                return claimsSet.getClaims().containsKey(JWT_ISSUER_KEY) && claimsSet.getClaims().get(JWT_ISSUER_KEY).equals(Constants.COGNITO_JWT_CLAIM_URL);
            } catch (Exception ex) {
                log.error("Failed to process auth token. Exception: " + ex.getMessage());
                return false;
            }
        }

        return false;
        //return (jwtParts.length == JWT_PARTS_COUNT && getJWTClaim(jwt, Constants.COGNITO_JWT_URL) != null);
    }

    //returns the decoded JWT Claim
    private String getJWTClaim(String jwt, String claim) {
        Map<String, String> jwtPayloadMap = new Gson().fromJson(getJWTPayload(jwt), HashMap.class);

        //return the claim value if it was found, otherwise return null
        return jwtPayloadMap.getOrDefault(claim, null);
    }

    //function to return the decoded JWT payload
    private String getJWTPayload(String jwt) {
        //Get payload from jwt
        String jwtPayload = jwt.split(JWT_SPLIT_PATTERN)[JWT_PAYLOAD_INDEX];

        //decode the payload
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedPayload = decoder.decode(jwtPayload);

        //format the payload as UTF_8 json string
        String formattedPayload = new String(decodedPayload, StandardCharsets.UTF_8);
        log.debug("JWT payload: " + formattedPayload);
        return formattedPayload;
    }

}
