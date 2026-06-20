package com.example.authorizationpoc.authz;

public enum Permission {
    CAN_VIEW("can_view"),
    CAN_CREATE_ASSIGNMENT("can_create_assignment"),
    CAN_UPDATE("can_update"),
    CAN_DELETE("can_delete"),
    CAN_MANAGE("can_manage");

    private final String relation;

    Permission(String relation) {
        this.relation = relation;
    }

    public String relation() {
        return relation;
    }
}
