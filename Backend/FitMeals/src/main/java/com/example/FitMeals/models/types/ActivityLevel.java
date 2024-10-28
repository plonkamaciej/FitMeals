package com.example.FitMeals.models.types;

public enum ActivityLevel {
    SEDENTARY,          // mało lub brak aktywności
    LIGHTLY_ACTIVE,     // lekka aktywność (1-3 dni w tygodniu)
    MODERATELY_ACTIVE,  // umiarkowana aktywność (3-5 dni w tygodniu)
    VERY_ACTIVE,        // wysoka aktywność (6-7 dni w tygodniu)
    EXTRA_ACTIVE;       // bardzo wysoka aktywność (codzienne treningi i praca fizyczna)

    public double getFactor() {
        switch (this) {
            case SEDENTARY:
                return 1.2;
            case LIGHTLY_ACTIVE:
                return 1.375;
            case MODERATELY_ACTIVE:
                return 1.55;
            case VERY_ACTIVE:
                return 1.725;
            case EXTRA_ACTIVE:
                return 1.9;
            default:
                throw new IllegalArgumentException("Unknown activity level: " + this);
        }
    }
}
