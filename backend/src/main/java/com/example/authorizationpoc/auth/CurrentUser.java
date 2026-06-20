package com.example.authorizationpoc.auth;

import java.util.UUID;

public record CurrentUser(
        UUID appUserId,
        String keycloakUserId,
        String username,
        String email,
        UUID tenantId,
        String tenantCode,
        String role
) {
}
