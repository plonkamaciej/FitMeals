package com.example.FitMeals.services;

import com.example.FitMeals.dto.FoodApiResponse;
import com.example.FitMeals.dto.FoodDto;
import com.example.FitMeals.dto.Hint;
import com.example.FitMeals.models.Food;
import com.example.FitMeals.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FoodApiService {

    private final RestTemplate restTemplate;
    private final FoodRepository foodRepository;

    public FoodApiService(FoodRepository foodRepository, RestTemplate restTemplate) {

        this.foodRepository = foodRepository;
        this.restTemplate = restTemplate;
    }

    @Value("${edamam.app_id}")
    private String appId;

    @Value("${edamam.app_key}")
    private String appKey;

    private final String API_URL = "https://api.edamam.com/api/food-database/v2/parser?ingr={ingredient}&app_id={app_id}&app_key={app_key}";

    public Food getFoodData(String ingredient) {
        String url = API_URL.replace("{ingredient}", ingredient)
                .replace("{app_id}", appId)
                .replace("{app_key}", appKey);


        FoodApiResponse response = restTemplate.getForObject(url, FoodApiResponse.class);

        if (response == null || response.getHints() == null || response.getHints().isEmpty()) {
            // Logowanie dla diagnostyki
            System.out.println("API response is null or does not contain any parsed data");
            return null;
        }

        Hint hint = response.getHints().get(0);
        FoodDto foodDto = hint.getFoodDto();

        Food food = new Food();
        food.setName(foodDto.getLabel());
        food.setCalories((int) foodDto.getNutrients().getENERC_KCAL());
        food.setProtein((int) foodDto.getNutrients().getPROCNT());
        food.setFat((int) foodDto.getNutrients().getFAT());
        food.setCarbs((int) foodDto.getNutrients().getCHOCDF());

        foodRepository.save(food);
        return food;
    }


    }

