package com.example.authorizationpoc.debug;

import com.example.authorizationpoc.auth.CurrentUser;
import com.example.authorizationpoc.auth.CurrentUserProvider;
import com.example.authorizationpoc.authz.AuthorizationService;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthzDebugController {

    private final CurrentUserProvider currentUserProvider;
    private final AuthorizationService authorizationService;

    public AuthzDebugController(CurrentUserProvider currentUserProvider, AuthorizationService authorizationService) {
        this.currentUserProvider = currentUserProvider;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/api/debug/authz/check")
    public Map<String, Object> check(@RequestParam @NotBlank String relation, @RequestParam @NotBlank String object) {
        CurrentUser user = currentUserProvider.getCurrentUser();
        boolean allowed = authorizationService.isAllowed(com.example.authorizationpoc.authz.Permission.valueOf(relation.toUpperCase()), object);
        return Map.of(
                "user", "user:" + user.username(),
                "relation", relation,
                "object", object,
                "allowed", allowed
        );
    }
}
