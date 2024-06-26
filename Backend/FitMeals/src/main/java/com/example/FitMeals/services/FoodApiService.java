package com.example.FitMeals.services;

import com.example.FitMeals.dto.FoodApiResponse;
import com.example.FitMeals.dto.FoodDto;
import com.example.FitMeals.dto.Parsed;
import com.example.FitMeals.models.Food;
import com.example.FitMeals.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FoodApiService {


    private final RestTemplate restTemplate;
    private final FoodRepository foodRepository;

    public FoodApiService(RestTemplate restTemplate, FoodRepository foodRepository) {
        this.restTemplate = restTemplate;
        this.foodRepository = foodRepository;
    }

    @Value("${edamam.app.id}")
    private String appId;

    @Value("${edamam.app.key}")
    private String appKey;

    private final String API_URL = "https://api.edamam.com/api/food-database/v2/parser?ingr={ingredient}&app_id={app_id}&app_key={app_key}";

    public Food getFoodData(String ingredient) {
        String url = API_URL.replace("{ingredient}", ingredient)
                .replace("{app_id}", appId)
                .replace("{app_key}", appKey);

        FoodApiResponse response = restTemplate.getForObject(url, FoodApiResponse.class);

        if (response != null && !response.getParsedList().isEmpty()) {
            Parsed parsed = response.getParsedList().get(0);
            FoodDto foodDto = parsed.getFoodDto();

            Food food = new Food();
            food.setName(foodDto.getLabel());
            food.setCalories((int) foodDto.getNutrients().getENERC_KCAL());
            food.setProtein((int) foodDto.getNutrients().getPROCNT());
            food.setFat((int) foodDto.getNutrients().getFAT());
            food.setCarbs((int) foodDto.getNutrients().getCHOCDF());

            foodRepository.save(food);
            return food;
        }

        return null;
    }
}
