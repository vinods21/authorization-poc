package com.example.authorizationpoc.report;

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
public class JdbcReportRepository implements ReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ReportRecord> findByIdAndTenantId(UUID id, UUID tenantId) {
        List<ReportRecord> results = jdbcTemplate.query(
                "select id, tenant_id, code, student_id, report_type, content from reports where id = ? and tenant_id = ?",
                mapper(),
                id,
                tenantId
        );
        return results.stream().findFirst();
    }

    @Override
    public ReportRecord save(ReportRecord reportRecord) {
        jdbcTemplate.update(
                "insert into reports (id, tenant_id, code, student_id, report_type, content) values (?, ?, ?, ?, ?, ?) on conflict (tenant_id, code) do update set student_id = excluded.student_id, report_type = excluded.report_type, content = excluded.content",
                reportRecord.id(),
                reportRecord.tenantId(),
                reportRecord.code(),
                reportRecord.studentId(),
                reportRecord.reportType(),
                reportRecord.content()
        );
        return reportRecord;
    }

    private RowMapper<ReportRecord> mapper() {
        return (ResultSet rs, int rowNum) -> new ReportRecord(
                rs.getObject("id", UUID.class),
                rs.getObject("tenant_id", UUID.class),
                rs.getString("code"),
                rs.getObject("student_id", UUID.class),
                rs.getString("report_type"),
                rs.getString("content")
        );
    }
}
