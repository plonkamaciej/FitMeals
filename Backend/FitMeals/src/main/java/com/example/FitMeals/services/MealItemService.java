package com.example.FitMeals.services;

import com.example.FitMeals.models.MealItem;
import com.example.FitMeals.repositories.MealItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealItemService {

    private final MealItemRepository mealItemRepository;

    public MealItemService(MealItemRepository mealItemRepository) {
        this.mealItemRepository = mealItemRepository;
    }

    public List<MealItem> getAllMealItems() {
        return mealItemRepository.findAll();
    }


    public Optional<MealItem> getMealItemById(Long id){
        return mealItemRepository.findById(id);
    }

    public MealItem saveMealItem(MealItem mealItem) {
        return mealItemRepository.save(mealItem);
    }

    public void deleteMealItem(Long id) {
        mealItemRepository.deleteById(id);
    }

    public Optional<MealItem> updateMealItem(MealItem mealItem) {
        Optional<MealItem> mealItemOptional = mealItemRepository.findById(mealItem.getId());
        if (mealItemOptional.isPresent()) {
            mealItemRepository.save(mealItem);
        }
        return mealItemOptional;
    }

}
