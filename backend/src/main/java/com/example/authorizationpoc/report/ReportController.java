package com.example.authorizationpoc.report;

import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping("/api/reports/{reportId}")
    public ReportRecord get(@PathVariable UUID reportId) {
        return service.getById(reportId);
    }
}
