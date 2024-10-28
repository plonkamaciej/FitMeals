package com.example.FitMeals.models;

import com.example.FitMeals.models.types.MealType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Food> foodList;

    public Meal(MealType mealType, List<Food> foodList) {
        this.mealType = mealType;
        this.foodList = foodList;
    }

    public void addFood(Food food) {
        food.setMeal(this);
        this.foodList.add(food);
    }

}
