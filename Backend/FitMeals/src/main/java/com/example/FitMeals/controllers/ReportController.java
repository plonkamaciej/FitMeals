package com.example.FitMeals.controllers;


import com.example.FitMeals.models.Report;
import com.example.FitMeals.services.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public Report getReportById(@PathVariable Long id) {
        return reportService.getReportById(id).orElse(null);
    }

    @PostMapping
    public Report createReport(@RequestBody Report report) {
        return reportService.saveReport(report);
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
}
