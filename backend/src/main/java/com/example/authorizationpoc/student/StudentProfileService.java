package com.example.authorizationpoc.student;

import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import com.example.authorizationpoc.authz.AuthorizationService;
import com.example.authorizationpoc.authz.Permission;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class StudentProfileService {

    private final StudentProfileRepository repository;
    private final CurrentUserProvider currentUserProvider;
    private final AuthorizationService authorizationService;

    public StudentProfileService(
            StudentProfileRepository repository,
            CurrentUserProvider currentUserProvider,
            AuthorizationService authorizationService
    ) {
        this.repository = repository;
        this.currentUserProvider = currentUserProvider;
        this.authorizationService = authorizationService;
    }

    public StudentProfile getById(UUID studentId) {
        CurrentUser currentUser = currentUserProvider.getCurrentUser();
        StudentProfile studentProfile = repository.findByIdAndTenantId(studentId, currentUser.tenantId())
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found"));
        authorizationService.check(Permission.CAN_VIEW, "student_profile:" + currentUser.tenantCode() + "/" + studentProfile.code());
        return studentProfile;
    }
}
