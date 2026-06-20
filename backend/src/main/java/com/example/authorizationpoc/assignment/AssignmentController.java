package com.example.authorizationpoc.assignment;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignmentController {

    private final AssignmentService service;

    public AssignmentController(AssignmentService service) {
        this.service = service;
    }

    @PostMapping("/api/classes/{classId}/assignments")
    public Assignment create(@PathVariable UUID classId, @RequestBody CreateAssignmentRequest request) {
        return service.createForClass(classId, request.code(), request.title(), request.description());
    }

    public record CreateAssignmentRequest(@NotBlank String code, @NotBlank String title, String description) {
    }
}
