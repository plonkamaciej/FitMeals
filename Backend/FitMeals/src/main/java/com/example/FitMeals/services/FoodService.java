package com.example.FitMeals.services;

import com.example.FitMeals.models.Food;
import com.example.FitMeals.models.Food;
import com.example.FitMeals.repositories.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }


    public Optional<Food> getFoodById(Long id){
        return foodRepository.findById(id);
    }

    public Food saveFood(Food food) {
        return foodRepository.save(food);
    }

    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
    }

    public Optional<Food> updateFood(Food food) {
        Optional<Food> foodOptional = foodRepository.findById(food.getId());
        if (foodOptional.isPresent()) {
            foodRepository.save(food);
        }
        return foodOptional;
    }
}
