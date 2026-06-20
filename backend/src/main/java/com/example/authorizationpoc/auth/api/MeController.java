package com.example.authorizationpoc.auth.api;

import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

    private final CurrentUserProvider currentUserProvider;

    public MeController(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/api/me")
    public Map<String, Object> me() {
        CurrentUser user = currentUserProvider.getCurrentUser();
        return Map.of(
                "userId", user.username(),
                "tenantCode", user.tenantCode(),
                "displayName", user.username(),
                "roles", List.of(user.role()),
                "features", Map.of(
                        "canManageSchool", "admin".equals(user.role()),
                        "canCreateAssignment", List.of("admin", "teacher").contains(user.role()),
                        "canViewReports", List.of("admin", "teacher", "parent").contains(user.role())
                )
        );
    }
}
