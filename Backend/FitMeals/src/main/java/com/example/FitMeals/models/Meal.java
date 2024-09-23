package com.example.FitMeals.models;

import com.example.FitMeals.models.types.MealType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Meal {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private MealType mealType;
    private double totalCalories;
    private double totalProtein;
    private double totalFat;
    private double totalCarbs;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private List<MealItem> mealItems;

    public Meal(MealType mealType) {
        this.mealType = mealType;
        this.mealItems = new ArrayList<>();
    }

    public Meal( MealType mealType, double totalCalories, double totalProtein, double totalFat, double totalCarbs) {

        this.mealType = mealType;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalCarbs = totalCarbs;
    }

    public void addMealItem(MealItem mealItem) {
        this.mealItems.add(mealItem);
        totalCalories += mealItem.getFood().getCalories();
        totalProtein += mealItem.getFood().getProtein();
        totalFat += mealItem.getFood().getFat();
        totalCarbs += mealItem.getFood().getCarbs();
    }

}
