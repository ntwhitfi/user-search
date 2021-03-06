package ntwhitfi.user.search.request.authorizer.handler;

import com.amazonaws.util.StringUtils;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import ntwhitfi.user.search.request.authorizer.config.DaggerRequestAuthorizerComponent;
import ntwhitfi.user.search.request.authorizer.config.RequestAuthorizerComponent;
import ntwhitfi.user.search.request.authorizer.model.AuthPolicy;
import ntwhitfi.user.search.request.authorizer.model.AuthRequest;
import ntwhitfi.user.search.request.authorizer.model.AuthResponse;
import ntwhitfi.user.search.request.authorizer.service.IRequestAuthorizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class RequestAuthorizerHandler implements RequestHandler<AuthRequest, AuthPolicy> {
    private static String METHOD_ARN_SPLIT_PATTERN = ":";
    private static String API_ARN_PARTIAL_SPLIT_PATTERN = "/";
    private Gson gson = new Gson();

    private IRequestAuthorizer requestAuthorizer;

    public RequestAuthorizerHandler() {
        this(DaggerRequestAuthorizerComponent.create());
    }

    public RequestAuthorizerHandler(final RequestAuthorizerComponent requestAuthorizerComponent) {
        this(requestAuthorizerComponent.getRequestAuthorizer());
    }

    public RequestAuthorizerHandler(IRequestAuthorizer requestAuthorizer) {
        this.requestAuthorizer = requestAuthorizer;
    }

    @Override
    public AuthPolicy handleRequest(AuthRequest authRequest, Context context) {
        log.info("Attempting to validate auth request: " + gson.toJson(authRequest));

        //if no auth token is provided, log an error an and return a null response
        if(StringUtils.isNullOrEmpty(authRequest.getAuthorizationToken())) {
            log.error("Invalid auth request. Could not retrieve auth token.");
            return null;
        }

        String authToken = authRequest.getAuthorizationToken();
        String principalId = this.requestAuthorizer.getJWTPrincipalId(authToken);
        String effect = AuthPolicy.DENY_EFFECT;

        log.info("Attempting to validate auth token.");
        //Validate the auth token and create the AuthPolicy
        AuthResponse authResponse = this.requestAuthorizer.validateAuthToken(authToken);

        if(authResponse.getStatusCode() == 200) {
            log.info("Auth token validated successfully.");
            //update the effect to Allow since the auth token was validated
            effect = AuthPolicy.ALLOW_EFFECT;

            log.info(String.format("Generating auth policy with Effect %s and PrincipalId %s", effect, principalId));
            return generateAuthPolicy(authRequest, effect, principalId);
        }

        //log error and send a deny auth policy
        log.error("Invalid auth request. Unable to validate auth token provided.");
        return generateAuthPolicy(authRequest, effect, principalId);
    }

    //generate the auth policy to return for the token provided
    private AuthPolicy generateAuthPolicy(AuthRequest authRequest, String effect, String principalId) {
        //Get the ARN from the API Gateway request for the method being invoked
        //Example method ARN: "arn:aws:execute-api:<region>:<accountId>:<restApiId>/<apiStage>/<HTTPMethod>/<urlPath[0]>/<urlPath[n]>"
        String methodArn = authRequest.getMethodArn();

        //Split the ARN from the request to get the parts required for the auth policy generation
        String[] arnPartials = methodArn.split(METHOD_ARN_SPLIT_PATTERN);
        String region = System.getenv("REGION");
        String awsAccountId = arnPartials[4];

        //Get API gateway parts from method arn.
        //Example API Gateway parts: <restApiId>/<apiStage>/<HTTPMethod>/<urlPath[0]>/<urlPath[n]>
        List<String> apiGatewayArnPartials = new ArrayList<>();
        Collections.addAll(apiGatewayArnPartials, arnPartials[5].split(API_ARN_PARTIAL_SPLIT_PATTERN));

        String restApiId = apiGatewayArnPartials.get(0);
        String apiGatewayStage = apiGatewayArnPartials.get(1);
        String httpMethod = apiGatewayArnPartials.get(2);
        String resource = ""; // root resource

        //Resource is not required in the request, if one was provided, override the root resource
        if (apiGatewayArnPartials.size() > 3) {
            log.info("getting resource path");
            StringBuilder resourceBuilder = new StringBuilder();
            for (String apiUrlPart : apiGatewayArnPartials.subList(3, apiGatewayArnPartials.size())) {
                if(resourceBuilder.toString().equals("")) {
                    resourceBuilder.append(apiUrlPart);
                } else {
                    resourceBuilder.append("/");
                    resourceBuilder.append(apiUrlPart);
                }
            }

            resource = resourceBuilder.toString();
        }


        log.info("Resource path being accessed " + resource);

        log.info("Attempting to create auth policy document for methodArn " + methodArn);
        //Create new PolicyDocument with empty allow and deny statements
        AuthPolicy.PolicyDocument policyDoc = new AuthPolicy.PolicyDocument(region, awsAccountId, restApiId, apiGatewayStage);
        //Set the method execution permissions based on the effect passed in
        if(effect.equals(AuthPolicy.ALLOW_EFFECT)) {
            log.info(String.format("Adding allow statement to policy document for method %s on resource %s", httpMethod, resource));
            policyDoc.allowMethod(AuthPolicy.HttpMethod.valueOf(httpMethod), resource);
        } else {
            log.info(String.format("Adding allow statement to policy document for method %s on resource %s", httpMethod, resource));
            policyDoc.denyMethod(AuthPolicy.HttpMethod.valueOf(httpMethod), resource);
        }

        log.info("Auth policy document: " + gson.toJson(policyDoc));
        AuthPolicy authPolicy = new AuthPolicy(principalId, policyDoc);
        log.info("Auth policy being returned: " + gson.toJson(authPolicy));
        return authPolicy;
    }
}
