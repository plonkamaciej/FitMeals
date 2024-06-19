package com.example.FitMeals.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MealItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    private int quantity;


    public MealItem(Meal meal, Food food, int quantity) {
        this.meal = meal;
        this.food = food;
        this.quantity = quantity;
    }
}
