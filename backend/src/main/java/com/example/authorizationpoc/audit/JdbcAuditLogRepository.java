package com.example.authorizationpoc.audit;

import java.sql.Timestamp;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Primary
@Profile("docker")
public class JdbcAuditLogRepository implements AuditLogRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAuditLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AuditLogEntry save(AuditLogEntry entry) {
        jdbcTemplate.update(
                "insert into audit_logs (id, tenant_id, user_id, action, resource_type, resource_id, allowed, reason, created_at) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                entry.id(),
                entry.tenantId(),
                entry.userId(),
                entry.action(),
                entry.resourceType(),
                entry.resourceId(),
                entry.allowed(),
                entry.reason(),
                Timestamp.from(entry.createdAt())
        );
        return entry;
    }
}
