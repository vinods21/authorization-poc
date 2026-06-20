package com.example.authorizationpoc.tenant;

import java.util.UUID;

public record Tenant(UUID id, String code, String name) {
}
