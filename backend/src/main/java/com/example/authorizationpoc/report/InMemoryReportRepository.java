package com.example.authorizationpoc.report;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryReportRepository implements ReportRepository {

    private final Map<UUID, ReportRecord> data = new ConcurrentHashMap<>();

    @Override
    public Optional<ReportRecord> findByIdAndTenantId(UUID id, UUID tenantId) {
        return Optional.ofNullable(data.get(id)).filter(row -> row.tenantId().equals(tenantId));
    }

    @Override
    public ReportRecord save(ReportRecord reportRecord) {
        data.put(reportRecord.id(), reportRecord);
        return reportRecord;
    }
}
