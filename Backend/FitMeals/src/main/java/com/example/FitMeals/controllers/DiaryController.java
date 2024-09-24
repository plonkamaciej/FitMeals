package com.example.FitMeals.controllers;

import com.example.FitMeals.models.Diary;
import com.example.FitMeals.services.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    // Pobierz dziennik użytkownika dla określonej daty
    @GetMapping("/user/{userId}/date/{date}")
    public Diary getDiaryByDateAndUser(@PathVariable Long userId, @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return diaryService.getDiaryByDateAndUser(userId, localDate);
    }

    // Utwórz nowy dziennik
    @PostMapping
    public Diary createDiary(@RequestBody Diary diary) {
        return diaryService.saveDiary(diary);
    }

    // Zapisz lub zaktualizuj dziennik
    @PostMapping("/{userId}/{date}")
    public ResponseEntity<String> saveDiary(@PathVariable Long userId, @PathVariable String date, @RequestBody Diary diary) {
        LocalDate diaryDate = LocalDate.parse(date);
        diaryService.saveDiary(diary, diaryDate, userId);
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
