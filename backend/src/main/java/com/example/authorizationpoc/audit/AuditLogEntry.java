package com.example.authorizationpoc.audit;

import java.time.Instant;
import java.util.UUID;

public record AuditLogEntry(
        UUID id,
        UUID tenantId,
        UUID userId,
        String action,
        String resourceType,
        String resourceId,
        boolean allowed,
        String reason,
        Instant createdAt
) {
}
