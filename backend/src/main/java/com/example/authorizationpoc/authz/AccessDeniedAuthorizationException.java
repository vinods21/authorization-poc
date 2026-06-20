package com.example.authorizationpoc.authz;

import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedAuthorizationException extends AccessDeniedException {
    public AccessDeniedAuthorizationException(String msg) {
        super(msg);
    }
}
