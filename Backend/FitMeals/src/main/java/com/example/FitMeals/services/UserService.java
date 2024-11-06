package com.example.FitMeals.services;

import com.example.FitMeals.models.AppUser;
import com.example.FitMeals.models.types.Gender;
import com.example.FitMeals.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<AppUser> getUserById(Long id){
        return userRepository.findById(id);
    }

  public Optional<AppUser> getUserByUsername(String username){
        return userRepository.findByUsername(username);
  }

    public AppUser saveUser(AppUser user) {
        user.setRole("USER");
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<AppUser> updateUser(AppUser user) {
        Optional<AppUser> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            AppUser existingUser = userOptional.get();
            existingUser.setAge(user.getAge());
            existingUser.setWeight(user.getWeight());
            existingUser.setHeight(user.getHeight());
            existingUser.setActivityLevel(user.getActivityLevel());
            calculateDailyCalorieRequirement(existingUser);
            userRepository.save(existingUser);
        }
        return userOptional;
    }

    public void calculateDailyCalorieRequirement(AppUser user) {
        double bmr;
        if (user.getGender().equals(Gender.MALE)) {
            bmr = 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge());
        } else {
            bmr = 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
        }

        double activityFactor = user.getActivityLevel().getFactor();
        double dailyCalories = Math.round(bmr * activityFactor);
        user.setDailyCalorieRequirement(dailyCalories);

        // Wyliczanie makroskładników
        double proteinCalories = dailyCalories * 0.20; // 20% na białko
        double carbsCalories = dailyCalories * 0.50; // 50% na węglowodany
        double fatCalories = dailyCalories * 0.30; // 30% na tłuszcze

        // Przeliczenie na gramy: białko i węglowodany mają 4 kcal na gram, tłuszcze 9 kcal na gram
        user.setProteinGoal(Math.round(proteinCalories / 4));
        user.setCarbsGoal(Math.round(carbsCalories / 4));
        user.setFatGoal(Math.round(fatCalories / 9));
    }

}
