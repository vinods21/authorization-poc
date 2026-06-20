package com.example.authorizationpoc.schoolclass;

import jakarta.validation.constraints.NotBlank;

public record CreateClassRequest(
        @NotBlank String code,
        @NotBlank String name,
        String section
) {
}
