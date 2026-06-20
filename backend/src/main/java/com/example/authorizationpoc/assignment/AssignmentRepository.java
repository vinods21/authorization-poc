package com.example.authorizationpoc.assignment;

import java.util.Optional;
import java.util.UUID;

public interface AssignmentRepository {
    Optional<Assignment> findByIdAndTenantId(UUID id, UUID tenantId);

    Assignment save(Assignment assignment);
}
