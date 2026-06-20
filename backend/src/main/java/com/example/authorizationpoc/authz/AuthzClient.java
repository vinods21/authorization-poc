package com.example.authorizationpoc.authz;

public interface AuthzClient {
    boolean isAllowed(String user, String relation, String object);

    void check(String user, String relation, String object);

    void writeTuple(String user, String relation, String object);
}
