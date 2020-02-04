package ntwhitfi.user.search.request.authorizer.config;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import dagger.Module;
import dagger.Provides;
import ntwhitfi.user.search.request.authorizer.service.IRequestAuthorizer;
import ntwhitfi.user.search.request.authorizer.service.RequestAuthorizer;
import ntwhitfi.user.search.request.authorizer.util.AuthTokenProcessor;

import javax.inject.Singleton;

@Module
public class RequestAuthorizerModule {

    @Provides
    @Singleton
    public ConfigurableJWTProcessor<SecurityContext> getJWTProcessor() {
        return AuthTokenProcessor.getJWTProcessor();
    }

    @Provides
    @Singleton
    public IRequestAuthorizer getRequestAuthorizer() {
        return new RequestAuthorizer();
    }
}
