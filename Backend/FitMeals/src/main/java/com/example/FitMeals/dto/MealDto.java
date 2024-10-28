package com.example.FitMeals.dto;

import com.example.FitMeals.models.Food;
import com.example.FitMeals.models.types.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MealDto {

    private Long id;
    private MealType mealType;
    private double total_calories;
    private double total_carbs;
    private double total_fat;
    private double total_protein;
    private List<Food> foodList;

}
