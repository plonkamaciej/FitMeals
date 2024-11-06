package com.example.FitMeals.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyRequirements {
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
}
