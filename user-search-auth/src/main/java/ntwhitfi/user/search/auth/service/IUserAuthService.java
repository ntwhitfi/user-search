package ntwhitfi.user.search.auth.service;

import ntwhitfi.user.search.auth.model.AuthResponse;

public interface IUserAuthService {
    AuthResponse login(String username, String password);
}
