package com.example.FitMeals.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double calories;
    private double protein;
    private double fat;
    private double carbs;
    private Double weight =100D;
    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;
//    @OneToMany(mappedBy = "food")
//    private Set<MealItem> mealItems;


    public Food(String name, double calories, double protein, double fat, double carbs, Double weight) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.weight = weight;
    }

    public Food(String name, double calories, double protein, double fat, double carbs, double weight) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.weight =weight;
    }

    public void setMacros(){
        this.setCalories(this.calories*this.weight/100);
        this.setProtein(this.protein*this.weight/100);
        this.setFat(this.fat*this.weight/100);
        this.setCarbs(this.carbs*this.weight/100);
    }

}
