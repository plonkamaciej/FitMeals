package com.example.FitMeals.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReportDto {
    private double totalCalories;
    private double totalProtein;
    private double totalFat;
    private double totalCarbs;
    private LocalDate startDate;
    private LocalDate endDate;

    public ReportDto(double totalCalories, double totalProtein, double totalFat, double totalCarbs, LocalDate startDate, LocalDate endDate) {
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalCarbs = totalCarbs;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
