package com.example.authorizationpoc.assignment;

import java.util.UUID;

public record Assignment(
        UUID id,
        UUID tenantId,
        String code,
        UUID classId,
        String title,
        String description,
        UUID createdBy
) {
}
