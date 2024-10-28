package com.example.FitMeals.repositories;

import com.example.FitMeals.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food,Long> {

    Optional<Food> getFoodByName(String name);
}
