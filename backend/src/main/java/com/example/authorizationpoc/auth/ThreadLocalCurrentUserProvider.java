package com.example.authorizationpoc.auth;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class ThreadLocalCurrentUserProvider implements CurrentUserProvider {

    @Override
    public CurrentUser getCurrentUser() {
        return TenantContext.get().orElseThrow(() -> new AccessDeniedException("No authenticated user in context"));
    }
}
