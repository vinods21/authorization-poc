package com.example.authorizationpoc.authz;

import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import com.example.authorizationpoc.audit.AuditService;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final AuthzClient authzClient;
    private final CurrentUserProvider currentUserProvider;
    private final AuditService auditService;

    public AuthorizationService(AuthzClient authzClient, CurrentUserProvider currentUserProvider, AuditService auditService) {
        this.authzClient = authzClient;
        this.currentUserProvider = currentUserProvider;
        this.auditService = auditService;
    }

    public boolean isAllowed(Permission permission, String object) {
        CurrentUser user = currentUserProvider.getCurrentUser();
        boolean allowed = authzClient.isAllowed(user.username(), permission.relation(), object);
        auditService.log(user, permission.name(), resourceType(object), object, allowed, allowed ? "OpenFGA allowed" : "OpenFGA denied");
        return allowed;
    }

    public void check(Permission permission, String object) {
        CurrentUser user = currentUserProvider.getCurrentUser();
        boolean allowed = authzClient.isAllowed(user.username(), permission.relation(), object);
        auditService.log(user, permission.name(), resourceType(object), object, allowed,
                allowed ? "OpenFGA allowed" : "OpenFGA denied " + user.username() + " " + permission.relation() + " " + object);
        if (!allowed) {
            throw new AccessDeniedAuthorizationException(
                    "OpenFGA denied " + user.username() + " " + permission.relation() + " " + object
            );
        }
    }

    private String resourceType(String object) {
        int colonIndex = object.indexOf(':');
        return colonIndex > 0 ? object.substring(0, colonIndex) : object;
    }
}
