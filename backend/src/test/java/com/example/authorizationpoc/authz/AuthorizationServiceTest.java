package com.example.authorizationpoc.authz;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.authorizationpoc.audit.AuditService;
import com.example.authorizationpoc.audit.InMemoryAuditLogRepository;
import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AuthorizationServiceTest {

    private static final CurrentUser USER = new CurrentUser(
            UUID.fromString("00000000-0000-0000-0000-000000000002"),
            "teacher-a",
            "teacher-a",
            "teacher-a@school-a.local",
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            "school-a",
            "teacher"
    );

    private static final CurrentUserProvider CURRENT_USER_PROVIDER = () -> USER;

    @Test
    void deniedCheckThrowsForbidden() {
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

        AuthorizationService service = new AuthorizationService(authzClient, CURRENT_USER_PROVIDER, new AuditService(new InMemoryAuditLogRepository()));

        assertThrows(AccessDeniedAuthorizationException.class, () -> service.check(Permission.CAN_VIEW, "class:school-a/class-10a"));
    }

    @Test
    void allowedCheckReturnsTrue() {
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

        AuthorizationService service = new AuthorizationService(authzClient, CURRENT_USER_PROVIDER, new AuditService(new InMemoryAuditLogRepository()));

        service.check(Permission.CAN_VIEW, "class:school-a/class-10a");
    }
}
