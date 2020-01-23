package ntwhitfi.user.search.config;

import dagger.Component;
import ntwhitfi.user.search.service.IUserSearchService;

import javax.inject.Singleton;

@Singleton
@Component(modules = UserSearchModule.class)
public interface UserSearchComponent {
    IUserSearchService userSearchService();
}
