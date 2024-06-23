package com.example.FitMeals.repositories;

import com.example.FitMeals.models.MealItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem,Long> {
}
