package com.example.FitMeals.services;

import com.example.FitMeals.models.AppUser;
import com.example.FitMeals.models.Diary;
import com.example.FitMeals.models.types.MealType;
import com.example.FitMeals.repositories.DiaryRepository;
import com.example.FitMeals.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    public DiaryService(DiaryRepository diaryRepository, UserRepository userRepository) {
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
    }

    public Diary getDiaryByDateAndUser(Long userId, LocalDate date){
        return diaryRepository.findDiaryByUserIdAndDate(userId,date);
    }

    public Diary saveDiary(Diary diary) {
        return diaryRepository.save(diary);
    }

    public void saveDiary(Diary diary, LocalDate date, Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Logika zapisu dziennika dla danego użytkownika
        diary.setUser(user);
        diary.setDate(date);

        diaryRepository.save(diary);
    }

    public Optional<Diary> getDiaryById(Long id) {
        return diaryRepository.findById(id);
    }

    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }


}
