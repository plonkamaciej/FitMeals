package com.example.FitMeals.services;

import com.example.FitMeals.models.Meal;
import com.example.FitMeals.repositories.MealRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealService {

    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }


   public Optional<Meal> getMealById(Long id){
        return mealRepository.findById(id);
    }

    public Meal saveMeal(Meal meal) {
        return mealRepository.save(meal);
    }

    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }

    public Optional<Meal> updateMeal(Meal meal) {
        Optional<Meal> mealOptional = mealRepository.findById(meal.getId());
        if (mealOptional.isPresent()) {
            mealRepository.save(meal);
        }
        return mealOptional;
    }
}
