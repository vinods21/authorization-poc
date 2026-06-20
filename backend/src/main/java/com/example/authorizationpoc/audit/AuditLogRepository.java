package com.example.authorizationpoc.audit;

public interface AuditLogRepository {
    AuditLogEntry save(AuditLogEntry entry);
}
