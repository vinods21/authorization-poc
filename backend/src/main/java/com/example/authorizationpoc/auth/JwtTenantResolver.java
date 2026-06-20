package com.example.authorizationpoc.auth;

import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtTenantResolver {

    public CurrentUser resolve(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String tenantCode = jwt.getClaimAsString("tenant_code");
        String role = jwt.getClaimAsString("role");
        String keycloakUserId = jwt.getSubject();
        String tenantIdValue = jwt.getClaimAsString("tenant_id");
        String appUserIdValue = jwt.getClaimAsString("app_user_id");

        return new CurrentUser(
                UUID.fromString(appUserIdValue),
                keycloakUserId,
                username,
                email,
                UUID.fromString(tenantIdValue),
                tenantCode,
                role
        );
    }
}
