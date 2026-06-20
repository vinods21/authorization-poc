package com.example.authorizationpoc.schoolclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!docker")
public class InMemorySchoolClassRepository implements SchoolClassRepository {

    private final Map<UUID, SchoolClass> data = new ConcurrentHashMap<>();

    @Override
    public Optional<SchoolClass> findByIdAndTenantId(UUID id, UUID tenantId) {
        return Optional.ofNullable(data.get(id)).filter(row -> row.tenantId().equals(tenantId));
    }

    @Override
    public Optional<SchoolClass> findByCodeAndTenantId(String code, UUID tenantId) {
        return data.values().stream()
                .filter(row -> row.tenantId().equals(tenantId) && row.code().equals(code))
                .findFirst();
    }

    @Override
    public List<SchoolClass> findAllByTenantId(UUID tenantId) {
        return data.values().stream().filter(row -> row.tenantId().equals(tenantId)).toList();
    }

    @Override
    public SchoolClass save(SchoolClass schoolClass) {
        data.put(schoolClass.id(), schoolClass);
        return schoolClass;
    }
}
