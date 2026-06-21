package com.example.authorizationpoc.schoolclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            "admin-a",
            "admin-a",
            "admin-a@school-a.local",
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            "school-a",
            "admin"
    );

    private static final CurrentUser TEACHER_B = new CurrentUser(
            UUID.fromString("00000000-0000-0000-0000-000000000005"),
            "teacher-b",
            "teacher-b",
            "teacher-b@school-b.local",
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            "school-b",
            "teacher"
    );

    private static CurrentUserProvider provider(CurrentUser currentUser) {
        return () -> currentUser;
    }

    private SchoolClassService service(CurrentUser currentUser, AuthzClient authzClient, SchoolClassRepository repository) {
        CurrentUserProvider currentUserProvider = provider(currentUser);
        AuthorizationService authorizationService = new AuthorizationService(authzClient, currentUserProvider, new AuditService(new InMemoryAuditLogRepository()));
        return new SchoolClassService(repository, currentUserProvider, authorizationService, authzClient, new AuditService(new InMemoryAuditLogRepository()));
    }

    @Test
    void adminCanCreateClassInSchoolA() {
        AuthzClient authzClient = new AuthzClient() {
            @Override
            public boolean isAllowed(String user, String relation, String object) {
                return true;
            }

            @Override
            public void check(String user, String relation, String object) {
            }

            @Override
            public void writeTuple(String user, String relation, String object) {
            }
        };
        SchoolClassRepository repository = new InMemorySchoolClassRepository();
        SchoolClassService service = service(ADMIN_A, authzClient, repository);

        SchoolClass created = service.create(new CreateClassRequest("class-11a", "Class 11A", "A"));

        assertEquals("class-11a", created.code());
        assertEquals(ADMIN_A.tenantId(), created.tenantId());
    }

    @Test
    void teacherBCannotViewSchoolAClass() {
        AuthzClient authzClient = new AuthzClient() {
            @Override
            public boolean isAllowed(String user, String relation, String object) {
                return false;
            }

            @Override
            public void check(String user, String relation, String object) {
            }

            @Override
            public void writeTuple(String user, String relation, String object) {
            }
        };
        SchoolClassRepository repository = new InMemorySchoolClassRepository();
        repository.save(new SchoolClass(UUID.fromString("00000000-0000-0000-0000-000000000031"), TEACHER_B.tenantId(), "class-10a", "Class 10A", "A", TEACHER_B.appUserId()));
        SchoolClassService service = service(TEACHER_B, authzClient, repository);

        assertThrows(com.example.authorizationpoc.authz.AccessDeniedAuthorizationException.class,
                () -> service.getById(UUID.fromString("00000000-0000-0000-0000-000000000031")));
    }
}
