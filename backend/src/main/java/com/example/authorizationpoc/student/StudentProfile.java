package com.example.authorizationpoc.student;

import java.util.UUID;

public record StudentProfile(
        UUID id,
        UUID tenantId,
        String code,
        UUID classId,
        String name,
        UUID ownerUserId
) {
}
