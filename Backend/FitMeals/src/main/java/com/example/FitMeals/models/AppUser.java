package com.example.FitMeals.models;

import com.example.FitMeals.models.types.ActivityLevel;
import com.example.FitMeals.models.types.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    @Column(nullable = false,columnDefinition = "integer default 0")
    private int age;
    //@Column(nullable = false,columnDefinition = "double default 0")
    private double weight;
    //@Column(nullable = false,columnDefinition = "integer default 0")
    private double height;
    //@Column(nullable = false,columnDefinition = "integer default 0")
    private Gender gender;
    //Column(nullable = false,columnDefinition = "integer default 0")
    private ActivityLevel activityLevel;
   // @Column(nullable = false,columnDefinition = "integer default 0")
    private double dailyCalorieRequirement;



    public AppUser(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public AppUser(String username, String password, String email, int age, double weight, double height, Gender gender, ActivityLevel activityLevel) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.activityLevel = activityLevel;
    }
}
