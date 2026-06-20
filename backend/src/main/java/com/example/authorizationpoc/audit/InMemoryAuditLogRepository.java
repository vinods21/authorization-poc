package com.example.authorizationpoc.audit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!docker")
public class InMemoryAuditLogRepository implements AuditLogRepository {

    private final List<AuditLogEntry> entries = Collections.synchronizedList(new ArrayList<>());

    @Override
    public AuditLogEntry save(AuditLogEntry entry) {
        entries.add(entry);
        return entry;
    }

    public List<AuditLogEntry> entries() {
        return List.copyOf(entries);
    }
}
