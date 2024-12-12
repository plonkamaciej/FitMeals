package com.example.FitMeals.repositories;

import com.example.FitMeals.models.AppUser;
import com.example.FitMeals.models.Report;
import com.example.FitMeals.models.types.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {

    List<Report> findByUserAndReportTypeAndStartDateBetween(
            AppUser user, ReportType reportType, LocalDateTime startDate, LocalDateTime endDate
    );
}
