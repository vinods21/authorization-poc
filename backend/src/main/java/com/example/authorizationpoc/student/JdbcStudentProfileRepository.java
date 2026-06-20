package com.example.authorizationpoc.student;

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
public class JdbcStudentProfileRepository implements StudentProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStudentProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<StudentProfile> findByIdAndTenantId(UUID id, UUID tenantId) {
        List<StudentProfile> results = jdbcTemplate.query(
                "select id, tenant_id, code, class_id, name, owner_user_id from students where id = ? and tenant_id = ?",
                mapper(),
                id,
                tenantId
        );
        return results.stream().findFirst();
    }

    @Override
    public StudentProfile save(StudentProfile studentProfile) {
        jdbcTemplate.update(
                "insert into students (id, tenant_id, code, class_id, name, owner_user_id) values (?, ?, ?, ?, ?, ?) on conflict (tenant_id, code) do update set class_id = excluded.class_id, name = excluded.name, owner_user_id = excluded.owner_user_id",
                studentProfile.id(),
                studentProfile.tenantId(),
                studentProfile.code(),
                studentProfile.classId(),
                studentProfile.name(),
                studentProfile.ownerUserId()
        );
        return studentProfile;
    }

    private RowMapper<StudentProfile> mapper() {
        return (ResultSet rs, int rowNum) -> new StudentProfile(
                rs.getObject("id", UUID.class),
                rs.getObject("tenant_id", UUID.class),
                rs.getString("code"),
                rs.getObject("class_id", UUID.class),
                rs.getString("name"),
                rs.getObject("owner_user_id", UUID.class)
        );
    }
}
