package com.example.authorizationpoc.schoolclass;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchoolClassController {

    private final SchoolClassService service;

    public SchoolClassController(SchoolClassService service) {
        this.service = service;
    }

    @PostMapping("/api/classes")
    public SchoolClass create(@Valid @RequestBody CreateClassRequest request) {
        return service.create(request);
    }

    @GetMapping("/api/classes")
    public List<SchoolClass> list() {
        return service.listVisible();
    }

    @GetMapping("/api/classes/{classId}")
    public SchoolClass get(@PathVariable UUID classId) {
        return service.getById(classId);
    }
}
