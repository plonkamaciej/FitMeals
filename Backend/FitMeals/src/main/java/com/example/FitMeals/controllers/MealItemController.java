package com.example.FitMeals.controllers;


import com.example.FitMeals.models.MealItem;
import com.example.FitMeals.services.MealItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/mealItems")
public class MealItemController {

    private final MealItemService mealItemService;

    public MealItemController(MealItemService mealItemItemService) {
        this.mealItemService = mealItemItemService;
    }

    @GetMapping
    public List<MealItem> getAllMealItems() {
        return mealItemService.getAllMealItems();
    }

    @GetMapping("/{id}")
    public MealItem getMealItemById(@PathVariable Long id) {
        return mealItemService.getMealItemById(id).orElse(null);
    }

    @PostMapping
    public MealItem createMealItem(@RequestBody MealItem mealItem) {
        return mealItemService.saveMealItem(mealItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealItem> updateMealItem(@PathVariable Long id, @RequestBody MealItem mealItem) {
        if(mealItemService.getMealItemById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        mealItem.setId(id);
        mealItemService.saveMealItem(mealItem);
        return ResponseEntity.ok(mealItem);
    }

    @DeleteMapping("/{id}")
    public void deleteMealItem(@PathVariable Long id) {
        mealItemService.deleteMealItem(id);
    }
}
