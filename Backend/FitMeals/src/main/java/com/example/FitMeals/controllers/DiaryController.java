package com.example.FitMeals.controllers;

import com.example.FitMeals.models.Diary;
import com.example.FitMeals.models.Meal;
import com.example.FitMeals.models.types.MealType;
import com.example.FitMeals.services.DiaryService;
import com.example.FitMeals.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;
    private final UserService userService;

    public DiaryController(DiaryService diaryService, UserService userService) {
        this.diaryService = diaryService;
        this.userService = userService;
    }


    @GetMapping("/{userId}")
    public List<Diary> getAllDiaries(@PathVariable Long userId) {
        return diaryService.getAllDiariesByUserId(userId);
    }


    // Pobierz dziennik użytkownika dla określonej daty
    @GetMapping("/{userId}/{date}")
    public ResponseEntity<Diary> getDiaryByDate(@PathVariable Long userId,@PathVariable String date) {
        System.out.println("Received userId: " + userId);
        LocalDate diaryDate = LocalDate.parse(date);
        Optional<Diary> diary = diaryService.getDiaryByDateAndUser(userId, diaryDate);

        if (diary.isPresent()) {
            return ResponseEntity.ok(diary.get());
        } else {
            // Jeśli nie znaleziono dziennika na dany dzień, zwracamy pusty dziennik
            Diary emptyDiary = new Diary();
            emptyDiary.setDate(diaryDate);
            emptyDiary.setUser(userService.getUserById(userId).get());
            return ResponseEntity.ok(emptyDiary);
        }
    }

    // Utwórz nowy dziennik
//    @PostMapping
//    public Diary createDiary(@RequestBody Diary diary) {
//        return diaryService.saveDiary(diary);
//    }

    // Zapisz lub zaktualizuj dziennik
    @PostMapping
    public ResponseEntity<String> saveDiary( @RequestBody Diary diary) {
        diaryService.saveOrUpdateDiary(diary);
        return ResponseEntity.ok("Diary saved successfully");
    }




    // Aktualizuj dziennik
    @PutMapping("/{id}")
    public ResponseEntity<Diary> updateDiary(@PathVariable Long id, @RequestBody Diary diary) {
        Optional<Diary> existingDiary = diaryService.getDiaryById(id);
        if (existingDiary.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        diary.setId(id);
        diaryService.saveDiary(diary);
        return ResponseEntity.ok(diary);
    }


    @PutMapping("/{userId}/{date}")
    public ResponseEntity<Diary> updateMealInDiary(
            @PathVariable Long userId,
            @PathVariable String date,
            @RequestBody Meal updatedMeal) {

        // Konwersja daty z ciągu znaków
        LocalDate diaryDate = LocalDate.parse(date);

        // Pobierz istniejący dziennik dla użytkownika i daty
        Optional<Diary> existingDiaryOpt = diaryService.getDiaryByDateAndUser(userId, diaryDate);

        Diary diary;
        if (existingDiaryOpt.isPresent()) {
            // Jeśli dziennik istnieje, pobierz go
            diary = existingDiaryOpt.get();
        } else {
            // Jeśli dziennik nie istnieje, stwórz nowy
            diary = new Diary();
            diary.setDate(diaryDate);
            diary.setUser(userService.getUserById(userId).orElseThrow(() ->
                    new RuntimeException("Nie znaleziono użytkownika o podanym ID")));
        }

        // Zaktualizuj odpowiedni posiłek w dzienniku
        switch (updatedMeal.getMealType()) {
            case BREAKFAST:
                diary.setBreakfast(updatedMeal);
                break;
            case LUNCH:
                diary.setLunch(updatedMeal);
                break;
            case DINNER:
                diary.setDinner(updatedMeal);
                break;
            case SNACK:
                diary.setSnack(updatedMeal);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        // Zapisz zmiany w dzienniku
        Diary savedDiary = diaryService.saveDiary(diary);

        return ResponseEntity.ok(savedDiary);
    }





    // Usuń dziennik
    @DeleteMapping("/{id}")
    public void deleteDiary(@PathVariable Long id) {
        diaryService.deleteDiary(id);
    }
}
