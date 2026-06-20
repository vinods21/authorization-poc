package com.example.authorizationpoc.authz;

import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final AuthzClient authzClient;
    private final CurrentUserProvider currentUserProvider;

    public AuthorizationService(AuthzClient authzClient, CurrentUserProvider currentUserProvider) {
        this.authzClient = authzClient;
        this.currentUserProvider = currentUserProvider;
    }

    public boolean isAllowed(Permission permission, String object) {
        CurrentUser user = currentUserProvider.getCurrentUser();
        return authzClient.isAllowed(user.username(), permission.relation(), object);
    }

    public void check(Permission permission, String object) {
        CurrentUser user = currentUserProvider.getCurrentUser();
        authzClient.check(user.username(), permission.relation(), object);
    }
}
