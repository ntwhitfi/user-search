package ntwhitfi.user.search.auth.config;

import dagger.Component;
import ntwhitfi.user.search.auth.service.IUserAuthService;

import javax.inject.Singleton;

@Singleton
@Component(modules = UserAuthModule.class)
public interface UserAuthComponent {
    IUserAuthService authService();
}
