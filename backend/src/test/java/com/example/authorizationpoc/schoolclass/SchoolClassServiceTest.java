package com.example.authorizationpoc.schoolclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.authorizationpoc.audit.AuditService;
import com.example.authorizationpoc.audit.InMemoryAuditLogRepository;
import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import com.example.authorizationpoc.authz.AuthorizationService;
import com.example.authorizationpoc.authz.AuthzClient;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SchoolClassServiceTest {

    private static final CurrentUser ADMIN_A = new CurrentUser(
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1"),
            "admin-a",
            "admin-a",
            "admin-a@school-a.local",
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            "school-a",
            "admin"
    );

    private static final CurrentUser TEACHER_B = new CurrentUser(
            UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb5"),
            "teacher-b",
            "teacher-b",
            "teacher-b@school-b.local",
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            "school-b",
            "teacher"
    );

    private SchoolClassService service(CurrentUser currentUser, AuthzClient authzClient, SchoolClassRepository repository) {
        CurrentUserProvider currentUserProvider = mock(CurrentUserProvider.class);
        when(currentUserProvider.getCurrentUser()).thenReturn(currentUser);
        AuthorizationService authorizationService = new AuthorizationService(authzClient, currentUserProvider, new AuditService(new InMemoryAuditLogRepository()));
        return new SchoolClassService(repository, currentUserProvider, authorizationService, authzClient, new AuditService(new InMemoryAuditLogRepository()));
    }

    @Test
    void adminCanCreateClassInSchoolA() {
        AuthzClient authzClient = mock(AuthzClient.class);
        SchoolClassRepository repository = new InMemorySchoolClassRepository();
        SchoolClassService service = service(ADMIN_A, authzClient, repository);

        SchoolClass created = service.create(new CreateClassRequest("class-11a", "Class 11A", "A"));

        assertEquals("class-11a", created.code());
        assertEquals(ADMIN_A.tenantId(), created.tenantId());
    }

    @Test
    void teacherBCannotViewSchoolAClass() {
        AuthzClient authzClient = mock(AuthzClient.class);
        when(authzClient.isAllowed("teacher-b", "can_view", "class:school-b/class-10a")).thenReturn(false);
        SchoolClassRepository repository = new InMemorySchoolClassRepository();
        repository.save(new SchoolClass(UUID.fromString("33333333-3333-3333-3333-333333333331"), TEACHER_B.tenantId(), "class-10a", "Class 10A", "A", TEACHER_B.appUserId()));
        SchoolClassService service = service(TEACHER_B, authzClient, repository);

        assertThrows(com.example.authorizationpoc.authz.AccessDeniedAuthorizationException.class,
                () -> service.getById(UUID.fromString("33333333-3333-3333-3333-333333333331")));
    }
}
