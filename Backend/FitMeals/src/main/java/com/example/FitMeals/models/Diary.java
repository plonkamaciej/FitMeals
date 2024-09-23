package com.example.FitMeals.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser user;

    private LocalDate date;

    @OneToOne(cascade = CascadeType.ALL)
    private Meal breakfast;

    @OneToOne(cascade = CascadeType.ALL)
    private Meal lunch;

    @OneToOne(cascade = CascadeType.ALL)
    private Meal dinner;

    @OneToOne(cascade = CascadeType.ALL)
    private Meal snack;

    private double totalCalories;
    private double totalProtein;
    private double totalFat;
    private double totalCarbs;
}
