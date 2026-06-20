package com.example.authorizationpoc.tenant;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!docker")
public class InMemoryTenantRepository implements TenantRepository {

    private static final Map<String, Tenant> TENANTS = Map.of(
            "school-a", new Tenant(UUID.fromString("11111111-1111-1111-1111-111111111111"), "school-a", "School A"),
            "school-b", new Tenant(UUID.fromString("22222222-2222-2222-2222-222222222222"), "school-b", "School B")
    );

    @Override
    public Optional<Tenant> findById(UUID id) {
        return TENANTS.values().stream().filter(tenant -> tenant.id().equals(id)).findFirst();
    }

    @Override
    public Optional<Tenant> findByCode(String code) {
        return Optional.ofNullable(TENANTS.get(code));
    }
}
