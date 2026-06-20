package com.example.authorizationpoc.authz;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@EnableConfigurationProperties(OpenFgaProperties.class)
public class OpenFgaAuthzClient implements AuthzClient {

    private final OpenFgaProperties properties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public OpenFgaAuthzClient(OpenFgaProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    }

    @Override
    public boolean isAllowed(String user, String relation, String object) {
        try {
            Map<String, Object> payload = Map.of(
                    "tuple_key", Map.of(
                            "user", "user:" + user,
                            "relation", relation,
                            "object", object
                    )
            );
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(properties.apiUrl() + "/stores/" + properties.storeId() + "/check"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                return false;
            }
            Map<?, ?> body = objectMapper.readValue(response.body(), Map.class);
            Object allowed = body.get("allowed");
            return Boolean.TRUE.equals(allowed);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to check OpenFGA authorization", ex);
        }
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
