package com.example.FitMeals.services;

import com.example.FitMeals.models.Diary;
import com.example.FitMeals.models.types.MealType;
import com.example.FitMeals.repositories.DiaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public Diary getDiaryByDateAndUser(Long userId, LocalDate date){
        return diaryRepository.findDiaryByUserIdAndDate(userId,date);
    }

    public Diary saveDiary(Diary diary) {
        return diaryRepository.save(diary);
    }

    public Optional<Diary> getDiaryById(Long id) {
        return diaryRepository.findById(id);
    }

    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }


}
