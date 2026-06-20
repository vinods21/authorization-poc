package com.example.authorizationpoc.assignment;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAssignmentRepository implements AssignmentRepository {

    private final Map<UUID, Assignment> data = new ConcurrentHashMap<>();

    @Override
    public Optional<Assignment> findByIdAndTenantId(UUID id, UUID tenantId) {
        return Optional.ofNullable(data.get(id)).filter(row -> row.tenantId().equals(tenantId));
    }

    @Override
    public Assignment save(Assignment assignment) {
        data.put(assignment.id(), assignment);
        return assignment;
    }
}
