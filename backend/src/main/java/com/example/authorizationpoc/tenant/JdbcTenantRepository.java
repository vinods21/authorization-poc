package com.example.authorizationpoc.tenant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Primary
@Profile("docker")
public class JdbcTenantRepository implements TenantRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTenantRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        List<Tenant> tenants = jdbcTemplate.query(
                "select id, code, name from tenants where id = ?",
                (rs, rowNum) -> new Tenant(rs.getObject("id", UUID.class), rs.getString("code"), rs.getString("name")),
                id
        );
        return tenants.stream().findFirst();
    }

    @Override
    public Optional<Tenant> findByCode(String code) {
        List<Tenant> tenants = jdbcTemplate.query(
                "select id, code, name from tenants where code = ?",
                (rs, rowNum) -> new Tenant(rs.getObject("id", UUID.class), rs.getString("code"), rs.getString("name")),
                code
        );
        return tenants.stream().findFirst();
    }
}
