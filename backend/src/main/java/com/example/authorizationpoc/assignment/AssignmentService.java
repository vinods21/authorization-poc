package com.example.authorizationpoc.assignment;

import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import com.example.authorizationpoc.authz.AuthorizationService;
import com.example.authorizationpoc.authz.Permission;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    private final AssignmentRepository repository;
    private final CurrentUserProvider currentUserProvider;
    private final AuthorizationService authorizationService;

    public AssignmentService(
            AssignmentRepository repository,
            CurrentUserProvider currentUserProvider,
            AuthorizationService authorizationService
    ) {
        this.repository = repository;
        this.currentUserProvider = currentUserProvider;
        this.authorizationService = authorizationService;
    }

    public Assignment createForClass(UUID classId, String code, String title, String description) {
        CurrentUser currentUser = currentUserProvider.getCurrentUser();
        authorizationService.check(Permission.CAN_CREATE_ASSIGNMENT, "class:" + currentUser.tenantCode() + "/" + classId);
        Assignment assignment = new Assignment(UUID.randomUUID(), currentUser.tenantId(), code, classId, title, description, currentUser.appUserId());
        return repository.save(assignment);
    }
}
