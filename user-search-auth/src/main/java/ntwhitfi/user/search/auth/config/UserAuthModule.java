package ntwhitfi.user.search.auth.config;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import dagger.Module;
import dagger.Provides;
import ntwhitfi.user.search.auth.service.IUserAuthService;
import ntwhitfi.user.search.auth.service.UserAuthService;

import javax.inject.Singleton;

@Module
public class UserAuthModule {
    private static String REGION = "us-east-1";

    @Provides
    @Singleton
    public AWSCognitoIdentityProvider cognitoIdentityClient() {
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(REGION)
                .build();
    }

    @Provides
    @Singleton
    public IUserAuthService userAuthService(AWSCognitoIdentityProvider cognitoIdentityClient) {
        return new UserAuthService(cognitoIdentityClient);
    }
}
