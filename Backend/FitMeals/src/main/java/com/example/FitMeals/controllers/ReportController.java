package com.example.FitMeals.controllers;


import com.example.FitMeals.dto.ReportRequest;
import com.example.FitMeals.models.AppUser;
import com.example.FitMeals.models.Report;
import com.example.FitMeals.models.types.ReportType;
import com.example.FitMeals.services.ReportService;
import com.example.FitMeals.services.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }


    @PutMapping("/{id}")
    public ResponseEntity<Report> updateReport(@PathVariable Long id, @RequestBody Report report) {
        if(reportService.getReportById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        report.setId(id);
        reportService.saveReport(report);
        return ResponseEntity.ok(report);
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }

    @PostMapping("/generate")
    public ResponseEntity<Report> generateReport(@RequestBody ReportRequest reportRequest
    ) {

        AppUser user = userService.getUserById(reportRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        Report report = reportService.generateReport(user, reportRequest.getReportType(), reportRequest.getStartDate(), reportRequest.getEndDate());
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public ResponseEntity<List<Report>> getReports(@RequestBody ReportRequest reportRequest) {
        // Pobierz użytkownika
        AppUser user = userService.getUserById(reportRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        List<Report> reports = reportService.getReports(user, reportRequest.getReportType(), reportRequest.getStartDate(), reportRequest.getEndDate());
        return ResponseEntity.ok(reports);
    }


}
