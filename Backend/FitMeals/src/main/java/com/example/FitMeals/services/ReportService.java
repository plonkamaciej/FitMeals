package com.example.FitMeals.services;

import com.example.FitMeals.models.Report;
import com.example.FitMeals.repositories.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }


    public Optional<Report> getReportById(Long id){
        return reportRepository.findById(id);
    }

    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    public Optional<Report> updateReport(Report report) {
        Optional<Report> reportOptional = reportRepository.findById(report.getId());
        if (reportOptional.isPresent()) {
            reportRepository.save(report);
        }
        return reportOptional;
    }
}
