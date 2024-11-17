package com.example.FitMeals.controllers;

import com.example.FitMeals.models.Food;
import com.example.FitMeals.models.Meal;
import com.example.FitMeals.services.FoodService;
import com.example.FitMeals.services.MealService;
import com.example.FitMeals.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;
    private final FoodService foodService;
    private final UserService userService;

    public MealController(MealService mealService, FoodService foodService, UserService userService) {
        this.mealService = mealService;
        this.foodService = foodService;
        this.userService = userService;
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
        for (Food food : mealToSave.getFoodList()) {
            food.setMeal(mealToSave); // Ustawiamy meal na każdy obiekt Food
        }
        return mealService.saveMeal(mealToSave);
    }

    @PostMapping("/{mealId}")
    public Meal addFood(@PathVariable Long mealId,@RequestBody Food food){
        Meal meal = mealService.getMealById(mealId).get();
        meal.addFood(food);
       return mealService.saveMeal(meal);
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
