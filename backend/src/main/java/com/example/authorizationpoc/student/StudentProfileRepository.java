package com.example.authorizationpoc.student;

import java.util.Optional;
import java.util.UUID;

public interface StudentProfileRepository {
    Optional<StudentProfile> findByIdAndTenantId(UUID id, UUID tenantId);

    StudentProfile save(StudentProfile studentProfile);
}
