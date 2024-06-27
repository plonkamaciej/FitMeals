package com.example.FitMeals.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Nutrients {

    @JsonProperty("ENERC_KCAL")
    private double ENERC_KCAL;

    @JsonProperty("PROCNT")
    private double PROCNT;

    @JsonProperty("FAT")
    private double FAT;

    @JsonProperty("CHOCDF")
    private double CHOCDF;
}
