package com.example.FitMeals.services;

import com.example.FitMeals.models.Food;
import com.example.FitMeals.models.Meal;
import com.example.FitMeals.models.MealItem;
import com.example.FitMeals.models.types.MealType;
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

    public void deleteAllMeals(){
        mealRepository.deleteAll();
    }

    public Meal updateMeal(Long id, List<Food> mealItems) {
        Meal meal = mealRepository.findById(id).get();
        for(Food m: mealItems){
                meal.addFood(m);
            }
            mealRepository.save(meal);

        return meal;
    }

    public Meal getMealByMealType(MealType mealType){
        return mealRepository.findMealByMealType(mealType);
    }
}
