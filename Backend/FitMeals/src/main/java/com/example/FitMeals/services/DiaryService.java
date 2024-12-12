package com.example.FitMeals.services;

import com.example.FitMeals.models.AppUser;
import com.example.FitMeals.models.Diary;
import com.example.FitMeals.models.types.MealType;
import com.example.FitMeals.repositories.DiaryRepository;
import com.example.FitMeals.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    public DiaryService(DiaryRepository diaryRepository, UserRepository userRepository) {
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
    }


    public List<Diary> getAllDiariesByUserId(Long userId){
        return diaryRepository.findAllByUserId(userId);
    }

    public Optional<Diary> getDiaryByDateAndUser(Long userId, LocalDate date){
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

    public void saveOrUpdateDiary(Diary diary) {
        // Sprawdzamy czy dziennik na dany dzień i użytkownika już istnieje
        Optional<Diary> existingDiary = diaryRepository.findDiaryByUserIdAndDate(diary.getUser().getId(), diary.getDate());
        AppUser user = userRepository.findById(diary.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (existingDiary.isPresent()) {
            // Aktualizacja istniejącego dziennika
            Diary dbDiary = existingDiary.get();
            dbDiary.setBreakfast(diary.getBreakfast());
            dbDiary.setLunch(diary.getLunch());
            dbDiary.setDinner(diary.getDinner());
            dbDiary.setSnack(diary.getSnack());
            diaryRepository.save(dbDiary);
        } else {
            // Tworzenie nowego dziennika

            diary.setUser(user);
            diaryRepository.save(diary);
        }
    }

    public Optional<Diary> getDiaryById(Long id) {
        return diaryRepository.findById(id);
    }

    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }


    public List<Diary>getDiaryByUserAndDateBetween(AppUser user, LocalDate startDate, LocalDate endDate){
        return diaryRepository.findByUserAndDateBetween(user,startDate,endDate);
    }


}
