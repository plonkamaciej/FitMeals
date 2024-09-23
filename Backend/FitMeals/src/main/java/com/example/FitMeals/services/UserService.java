package com.example.FitMeals.services;

import com.example.FitMeals.models.AppUser;
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
            userRepository.save(user);
        }
        return userOptional;
    }
}
