package com.example.authorizationpoc.report;

import java.util.Optional;
import java.util.UUID;

public interface ReportRepository {
    Optional<ReportRecord> findByIdAndTenantId(UUID id, UUID tenantId);

    ReportRecord save(ReportRecord reportRecord);
}
