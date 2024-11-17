package com.example.FitMeals.repositories;

import com.example.FitMeals.models.Meal;
import com.example.FitMeals.models.types.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal,Long> {

    Meal findMealByMealType(MealType mealType);


}
