package com.example.FitMeals.models;

import com.example.FitMeals.models.types.ReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private ReportType reportType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double totalCalories;
    private double totalProtein;
    private double totalFat;
    private double totalCarbs;
    private LocalDateTime createdAt;

    public Report(User user, ReportType reportType, LocalDateTime startDate, LocalDateTime endDate, double totalCalories, double totalProtein, double totalFat, double totalCarbs, LocalDateTime createdAt) {
        this.user = user;
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalCarbs = totalCarbs;
        this.createdAt = createdAt;
    }
}
