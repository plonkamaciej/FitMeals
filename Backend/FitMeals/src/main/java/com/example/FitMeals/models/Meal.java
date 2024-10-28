package com.example.FitMeals.models;

import com.example.FitMeals.models.types.MealType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private List<Food> foodList;

    public Meal(MealType mealType) {
        this.mealType = mealType;
        this.foodList = new ArrayList<>();
    }


    public void addFood(Food food) {
        this.foodList.add(food);
//        totalCalories += food.getCalories();
//        totalProtein += food.getProtein();
//        totalFat += food.getFat();
//        totalCarbs += food.getCarbs();
    }

}
