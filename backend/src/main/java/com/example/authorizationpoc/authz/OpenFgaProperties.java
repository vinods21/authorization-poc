package com.example.authorizationpoc.authz;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "authorization.openfga")
public record OpenFgaProperties(
        String apiUrl,
        String storeId
) {
}
