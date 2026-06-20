package com.example.authorizationpoc.authz;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class OpenFgaAuthzClient implements AuthzClient {

    @Override
    public boolean isAllowed(String user, String relation, String object) {
        return false;
    }

    @Override
    public void check(String user, String relation, String object) {
        if (!isAllowed(user, relation, object)) {
            throw new AccessDeniedAuthorizationException(
                    "OpenFGA denied " + user + " " + relation + " " + object
            );
        }
    }
}
