package com.example.FitMeals.controllers;

import com.example.FitMeals.models.Food;
import com.example.FitMeals.services.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public List<Food> getAllFoods() {
        return foodService.getAllFoods();
    }

    @GetMapping("/{id}")
    public Food getFoodById(@PathVariable Long id) {
        return foodService.getFoodById(id).orElse(null);
    }

    @PostMapping
    public Food createFood(@RequestBody Food food) {
        return foodService.saveFood(food);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFood(@PathVariable Long id, @RequestBody Food food) {
        if(foodService.getFoodById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        food.setId(id);
        foodService.saveFood(food);
        return ResponseEntity.ok(food);
    }

    @DeleteMapping("/{id}")
    public void deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
    }
}
