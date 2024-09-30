package com.example.FitMeals.repositories;

import com.example.FitMeals.models.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {

   Optional<Diary> findDiaryByUserIdAndDate(Long id, LocalDate date);

   Diary findDiaryByDate(LocalDate date);
}
