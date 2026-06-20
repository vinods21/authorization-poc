package com.example.authorizationpoc.student;

import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentProfileController {

    private final StudentProfileService service;

    public StudentProfileController(StudentProfileService service) {
        this.service = service;
    }

    @GetMapping("/api/students/{studentId}")
    public StudentProfile get(@PathVariable UUID studentId) {
        return service.getById(studentId);
    }
}
