package com.example.FitMeals.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MacroSummary {

    private double totalCalories;
    private double totalProtein;
    private double totalFat;
    private double totalCarbs;


}
