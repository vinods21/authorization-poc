package com.example.authorizationpoc.report;

import java.util.UUID;

public record ReportRecord(
        UUID id,
        UUID tenantId,
        String code,
        UUID studentId,
        String reportType,
        String content
) {
}
