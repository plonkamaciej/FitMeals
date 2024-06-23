package com.example.FitMeals.models;

import com.example.FitMeals.models.types.MealType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Meal {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    private LocalDateTime date;
    private MealType mealType;
    private double totalCalories;
    private double totalProtein;
    private double totalFat;
    private double totalCarbs;


    public Meal(AppUser user, LocalDateTime date, MealType mealType, double totalCalories, double totalProtein, double totalFat, double totalCarbs) {
        this.user = user;
        this.date = date;
        this.mealType = mealType;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalCarbs = totalCarbs;
    }



}
