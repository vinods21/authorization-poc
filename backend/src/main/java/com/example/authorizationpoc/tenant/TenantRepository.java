package com.example.authorizationpoc.tenant;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository {
    Optional<Tenant> findById(UUID id);

    Optional<Tenant> findByCode(String code);
}
