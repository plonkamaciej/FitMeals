package com.example.FitMeals.services;

import com.example.FitMeals.dto.MacroSummary;
import com.example.FitMeals.models.*;
import com.example.FitMeals.models.types.ReportType;
import com.example.FitMeals.repositories.DiaryRepository;
import com.example.FitMeals.repositories.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final DiaryRepository diaryRepository;

    public ReportService(ReportRepository reportRepository, DiaryRepository diaryRepository) {
        this.reportRepository = reportRepository;
        this.diaryRepository = diaryRepository;
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

    public MacroSummary calculateTotalMacros(List<Diary> diaries) {
        double totalCalories = 0;
        double totalProtein = 0;
        double totalFat = 0;
        double totalCarbs = 0;

        for (Diary diary : diaries) {
            // Pobierz wszystkie posiłki z dziennika
            Meal[] meals = {diary.getBreakfast(), diary.getLunch(), diary.getDinner(), diary.getSnack()};

            for (Meal meal : meals) {
                if (meal != null && meal.getFoodList() != null) {
                    for (Food food : meal.getFoodList()) {
                        totalCalories += food.getCalories();
                        totalProtein += food.getProtein();
                        totalFat += food.getFat();
                        totalCarbs += food.getCarbs();
                    }
                }
            }
        }

        return new MacroSummary(totalCalories, totalProtein, totalFat, totalCarbs);
    }




    public Report generateReport(AppUser user, ReportType reportType, LocalDateTime startDate, LocalDateTime endDate) {
        // Pobierz dane z dziennika użytkownika
        List<Diary> diaries = diaryRepository.findByUserAndDateBetween(user, startDate.toLocalDate(), endDate.toLocalDate());

        // Podsumuj makroskładniki

        MacroSummary macroSummary = calculateTotalMacros(diaries);

        // Utwórz raport
        Report report = new Report();
        report.setUser(user);
        report.setReportType(reportType);
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setTotalCalories(macroSummary.getTotalCalories());
        report.setTotalProtein(macroSummary.getTotalProtein());
        report.setTotalFat(macroSummary.getTotalFat());
        report.setTotalCarbs(macroSummary.getTotalCarbs());
        report.setCreatedAt(LocalDateTime.now());

        // Zapisz raport
        return report;
    }

    public List<Report> getReports(AppUser user, ReportType reportType, LocalDateTime startDate, LocalDateTime endDate) {
        return reportRepository.findByUserAndReportTypeAndStartDateBetween(user, reportType, startDate, endDate);
    }

}
