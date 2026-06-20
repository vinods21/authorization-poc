package com.example.authorizationpoc.assignment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Primary
@Profile("docker")
public class JdbcAssignmentRepository implements AssignmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAssignmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Assignment> findByIdAndTenantId(UUID id, UUID tenantId) {
        List<Assignment> results = jdbcTemplate.query(
                "select id, tenant_id, code, class_id, title, description, created_by from assignments where id = ? and tenant_id = ?",
                mapper(),
                id,
                tenantId
        );
        return results.stream().findFirst();
    }

    @Override
    public Assignment save(Assignment assignment) {
        jdbcTemplate.update(
                "insert into assignments (id, tenant_id, code, class_id, title, description, created_by) values (?, ?, ?, ?, ?, ?, ?) on conflict (tenant_id, code) do update set class_id = excluded.class_id, title = excluded.title, description = excluded.description, created_by = excluded.created_by",
                assignment.id(),
                assignment.tenantId(),
                assignment.code(),
                assignment.classId(),
                assignment.title(),
                assignment.description(),
                assignment.createdBy()
        );
        return assignment;
    }

    private RowMapper<Assignment> mapper() {
        return (ResultSet rs, int rowNum) -> new Assignment(
                rs.getObject("id", UUID.class),
                rs.getObject("tenant_id", UUID.class),
                rs.getString("code"),
                rs.getObject("class_id", UUID.class),
                rs.getString("title"),
                rs.getString("description"),
                rs.getObject("created_by", UUID.class)
        );
    }
}
