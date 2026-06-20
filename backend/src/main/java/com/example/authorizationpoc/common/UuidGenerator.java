package com.example.authorizationpoc.common;

import java.util.UUID;

public final class UuidGenerator {

    private UuidGenerator() {
    }

    public static UUID newUuid() {
        return UUID.randomUUID();
    }
}
