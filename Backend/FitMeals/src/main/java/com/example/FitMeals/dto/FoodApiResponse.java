package com.example.FitMeals.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FoodApiResponse {

    private List<Parsed> parsedList;
}
