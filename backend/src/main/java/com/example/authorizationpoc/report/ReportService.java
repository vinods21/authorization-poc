package com.example.authorizationpoc.report;

import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import com.example.authorizationpoc.audit.AuditService;
import com.example.authorizationpoc.authz.AuthorizationService;
import com.example.authorizationpoc.authz.Permission;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final ReportRepository repository;
    private final CurrentUserProvider currentUserProvider;
    private final AuthorizationService authorizationService;
    private final AuditService auditService;

    public ReportService(
            ReportRepository repository,
            CurrentUserProvider currentUserProvider,
            AuthorizationService authorizationService,
            AuditService auditService
    ) {
        this.repository = repository;
        this.currentUserProvider = currentUserProvider;
        this.authorizationService = authorizationService;
        this.auditService = auditService;
    }

    public ReportRecord getById(UUID reportId) {
        CurrentUser currentUser = currentUserProvider.getCurrentUser();
        ReportRecord reportRecord = repository.findByIdAndTenantId(reportId, currentUser.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));
        authorizationService.check(Permission.CAN_VIEW, "report:" + currentUser.tenantCode() + "/" + reportRecord.code());
        auditService.log(currentUser, "VIEW_REPORT", "report", "report:" + currentUser.tenantCode() + "/" + reportRecord.code(), true, "viewed");
        return reportRecord;
    }
}
