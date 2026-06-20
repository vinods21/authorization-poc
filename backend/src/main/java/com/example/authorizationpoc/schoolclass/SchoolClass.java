package com.example.authorizationpoc.schoolclass;

import java.util.UUID;

public record SchoolClass(
        UUID id,
        UUID tenantId,
        String code,
        String name,
        String section,
        UUID createdBy
) {
}
