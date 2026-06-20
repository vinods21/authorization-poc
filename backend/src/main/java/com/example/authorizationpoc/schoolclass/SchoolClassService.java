package com.example.authorizationpoc.schoolclass;

import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import com.example.authorizationpoc.authz.AuthorizationService;
import com.example.authorizationpoc.authz.Permission;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class SchoolClassService {

    private final SchoolClassRepository repository;
    private final CurrentUserProvider currentUserProvider;
    private final AuthorizationService authorizationService;

    public SchoolClassService(
            SchoolClassRepository repository,
            CurrentUserProvider currentUserProvider,
            AuthorizationService authorizationService
    ) {
        this.repository = repository;
        this.currentUserProvider = currentUserProvider;
        this.authorizationService = authorizationService;
    }

    public SchoolClass create(CreateClassRequest request) {
        CurrentUser currentUser = currentUserProvider.getCurrentUser();
        UUID id = UUID.randomUUID();
        SchoolClass schoolClass = new SchoolClass(
                id,
                currentUser.tenantId(),
                request.code(),
                request.name(),
                request.section(),
                currentUser.appUserId()
        );
        repository.save(schoolClass);
        return schoolClass;
    }

    public List<SchoolClass> listVisible() {
        CurrentUser currentUser = currentUserProvider.getCurrentUser();
        return repository.findAllByTenantId(currentUser.tenantId());
    }

    public SchoolClass getById(UUID classId) {
        CurrentUser currentUser = currentUserProvider.getCurrentUser();
        SchoolClass schoolClass = repository.findByIdAndTenantId(classId, currentUser.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        authorizationService.check(Permission.CAN_VIEW, "class:" + currentUser.tenantCode() + "/" + schoolClass.code());
        return schoolClass;
    }
}
