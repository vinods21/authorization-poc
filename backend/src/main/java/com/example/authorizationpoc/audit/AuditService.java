package com.example.authorizationpoc.audit;

import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.common.UuidGenerator;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void log(CurrentUser user, String action, String resourceType, String resourceId, boolean allowed, String reason) {
        repository.save(new AuditLogEntry(
                UuidGenerator.newUuid(),
                user == null ? null : user.tenantId(),
                user == null ? null : user.appUserId(),
                action,
                resourceType,
                resourceId,
                allowed,
                reason,
                Instant.now()
        ));
    }
}
