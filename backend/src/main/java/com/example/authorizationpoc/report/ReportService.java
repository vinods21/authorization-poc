package com.example.authorizationpoc.report;

import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import com.example.authorizationpoc.authz.AuthorizationService;
import com.example.authorizationpoc.authz.Permission;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final ReportRepository repository;
    private final CurrentUserProvider currentUserProvider;
    private final AuthorizationService authorizationService;

    public ReportService(
            ReportRepository repository,
            CurrentUserProvider currentUserProvider,
            AuthorizationService authorizationService
    ) {
        this.repository = repository;
        this.currentUserProvider = currentUserProvider;
        this.authorizationService = authorizationService;
    }

    public ReportRecord getById(UUID reportId) {
        CurrentUser currentUser = currentUserProvider.getCurrentUser();
        ReportRecord reportRecord = repository.findByIdAndTenantId(reportId, currentUser.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));
        authorizationService.check(Permission.CAN_VIEW, "report:" + currentUser.tenantCode() + "/" + reportRecord.code());
        return reportRecord;
    }
}
