package com.example.authorizationpoc.authz;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.authorizationpoc.audit.AuditService;
import com.example.authorizationpoc.audit.InMemoryAuditLogRepository;
import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AuthorizationServiceTest {

    @Test
    void deniedCheckThrowsForbidden() {
        CurrentUserProvider currentUserProvider = mock(CurrentUserProvider.class);
        when(currentUserProvider.getCurrentUser()).thenReturn(new CurrentUser(
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2"),
                "teacher-a",
                "teacher-a",
                "teacher-a@school-a.local",
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "school-a",
                "teacher"
        ));
        AuthzClient authzClient = mock(AuthzClient.class);
        when(authzClient.isAllowed("teacher-a", "can_view", "class:school-a/class-10a")).thenReturn(false);

        AuthorizationService service = new AuthorizationService(authzClient, currentUserProvider, new AuditService(new InMemoryAuditLogRepository()));

        assertThrows(AccessDeniedAuthorizationException.class, () -> service.check(Permission.CAN_VIEW, "class:school-a/class-10a"));
    }

    @Test
    void allowedCheckReturnsTrue() {
        CurrentUserProvider currentUserProvider = mock(CurrentUserProvider.class);
        when(currentUserProvider.getCurrentUser()).thenReturn(new CurrentUser(
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2"),
                "teacher-a",
                "teacher-a",
                "teacher-a@school-a.local",
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "school-a",
                "teacher"
        ));
        AuthzClient authzClient = mock(AuthzClient.class);
        when(authzClient.isAllowed("teacher-a", "can_view", "class:school-a/class-10a")).thenReturn(true);

        AuthorizationService service = new AuthorizationService(authzClient, currentUserProvider, new AuditService(new InMemoryAuditLogRepository()));

        service.check(Permission.CAN_VIEW, "class:school-a/class-10a");
    }
}
