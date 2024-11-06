package com.example.FitMeals.dto;

import com.example.FitMeals.models.types.ActivityLevel;
import com.example.FitMeals.models.types.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInputDto {

    private int age;
    private double weight;
    private double height;
    private Gender gender;
    private ActivityLevel activityLevel;
}
