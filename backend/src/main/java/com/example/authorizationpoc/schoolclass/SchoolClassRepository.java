package com.example.authorizationpoc.schoolclass;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SchoolClassRepository {
    Optional<SchoolClass> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<SchoolClass> findByCodeAndTenantId(String code, UUID tenantId);

    List<SchoolClass> findAllByTenantId(UUID tenantId);

    SchoolClass save(SchoolClass schoolClass);
}
