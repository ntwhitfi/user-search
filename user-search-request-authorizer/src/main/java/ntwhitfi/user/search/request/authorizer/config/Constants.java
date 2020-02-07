package ntwhitfi.user.search.request.authorizer.config;

public class Constants {
    private static String USER_POOL_ID = System.getenv("USER_POOL_ID");
    private static String REGION = System.getenv("REGION");
    public static String COGNITO_JWT_CLAIM_URL = String.format(
            "https://cognito-idp.%s.amazonaws.com/%s", REGION, USER_POOL_ID);
    public static String COGNITO_JWK_URL = String.format(
            "https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", REGION, USER_POOL_ID);
}
