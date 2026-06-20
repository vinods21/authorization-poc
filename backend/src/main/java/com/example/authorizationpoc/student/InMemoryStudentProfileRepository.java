package com.example.authorizationpoc.student;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryStudentProfileRepository implements StudentProfileRepository {

    private final Map<UUID, StudentProfile> data = new ConcurrentHashMap<>();

    @Override
    public Optional<StudentProfile> findByIdAndTenantId(UUID id, UUID tenantId) {
        return Optional.ofNullable(data.get(id)).filter(row -> row.tenantId().equals(tenantId));
    }

    @Override
    public StudentProfile save(StudentProfile studentProfile) {
        data.put(studentProfile.id(), studentProfile);
        return studentProfile;
    }
}
