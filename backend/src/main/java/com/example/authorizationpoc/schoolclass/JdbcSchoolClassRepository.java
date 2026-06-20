package com.example.authorizationpoc.schoolclass;

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
public class JdbcSchoolClassRepository implements SchoolClassRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcSchoolClassRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<SchoolClass> findByIdAndTenantId(UUID id, UUID tenantId) {
        List<SchoolClass> results = jdbcTemplate.query(
                "select id, tenant_id, code, name, section, created_by from school_classes where id = ? and tenant_id = ?",
                mapper(),
                id,
                tenantId
        );
        return results.stream().findFirst();
    }

    @Override
    public Optional<SchoolClass> findByCodeAndTenantId(String code, UUID tenantId) {
        List<SchoolClass> results = jdbcTemplate.query(
                "select id, tenant_id, code, name, section, created_by from school_classes where code = ? and tenant_id = ?",
                mapper(),
                code,
                tenantId
        );
        return results.stream().findFirst();
    }

    @Override
    public List<SchoolClass> findAllByTenantId(UUID tenantId) {
        return jdbcTemplate.query(
                "select id, tenant_id, code, name, section, created_by from school_classes where tenant_id = ? order by code",
                mapper(),
                tenantId
        );
    }

    @Override
    public SchoolClass save(SchoolClass schoolClass) {
        jdbcTemplate.update(
                "insert into school_classes (id, tenant_id, code, name, section, created_by) values (?, ?, ?, ?, ?, ?) on conflict (tenant_id, code) do update set name = excluded.name, section = excluded.section, created_by = excluded.created_by",
                schoolClass.id(),
                schoolClass.tenantId(),
                schoolClass.code(),
                schoolClass.name(),
                schoolClass.section(),
                schoolClass.createdBy()
        );
        return schoolClass;
    }

    private RowMapper<SchoolClass> mapper() {
        return new RowMapper<>() {
            @Override
            public SchoolClass mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new SchoolClass(
                        rs.getObject("id", UUID.class),
                        rs.getObject("tenant_id", UUID.class),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("section"),
                        rs.getObject("created_by", UUID.class)
                );
            }
        };
    }
}
