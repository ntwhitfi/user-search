package ntwhitfi.user.search.etl.config;

import dagger.Component;
import ntwhitfi.user.search.etl.IUserETLService;

import javax.inject.Singleton;

@Singleton
@Component(modules = UserETLModule.class)
public interface UserETLComponent {
    IUserETLService userETLService();
}
