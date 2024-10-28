package com.example.FitMeals.controllers;

import com.example.FitMeals.models.Meal;
import com.example.FitMeals.models.MealItem;
import com.example.FitMeals.models.types.MealType;
import com.example.FitMeals.services.FoodService;
import com.example.FitMeals.services.MealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;
    private final FoodService foodService;

    public MealController(MealService mealService, FoodService foodService) {
        this.mealService = mealService;
        this.foodService = foodService;
    }

    @GetMapping
    public List<Meal> getAllMeals() {
        return mealService.getAllMeals();
    }

    @GetMapping("/{id}")
    public Meal getMealById(@PathVariable Long id) {
        return mealService.getMealById(id).orElse(null);
    }

    @PostMapping
    public Meal createMeal(@RequestBody Meal mealToSave) {
        return mealService.saveMeal(mealToSave);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meal> updateMeal(@PathVariable Long id, @RequestBody Meal meal) {
        if(mealService.getMealById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        meal.setId(id);
        mealService.saveMeal(meal);
        return ResponseEntity.ok(meal);
    }

    @DeleteMapping("/{id}")
    public void deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
    }

    @DeleteMapping
    public void deleteAllMeals(){
        mealService.deleteAllMeals();
    }
}
