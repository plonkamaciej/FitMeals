package com.example.FitMeals.controllers;

import com.example.FitMeals.models.Diary;
import com.example.FitMeals.models.Meal;
import com.example.FitMeals.services.DiaryService;
import com.example.FitMeals.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    // Usuń dziennik
    @DeleteMapping("/{id}")
    public void deleteDiary(@PathVariable Long id) {
        diaryService.deleteDiary(id);
    }
}
