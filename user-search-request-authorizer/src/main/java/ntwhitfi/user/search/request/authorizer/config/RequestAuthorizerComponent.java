package ntwhitfi.user.search.request.authorizer.config;

import dagger.Component;
import ntwhitfi.user.search.request.authorizer.service.IRequestAuthorizer;

import javax.inject.Singleton;

@Singleton
@Component(modules = RequestAuthorizerModule.class)
public interface RequestAuthorizerComponent {

    IRequestAuthorizer getRequestAuthorizer();
}
