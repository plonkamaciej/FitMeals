package com.example.FitMeals.controllers;


import com.example.FitMeals.models.AppUser;
import com.example.FitMeals.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public AppUser getUserById(@PathVariable Long id) {
        return userService.getUserById(id).orElse(null);
    }


    @PostMapping("/addUser")
    public String addUser(@RequestBody AppUser user) {
        userService.saveUser(user);
        return "redirect:/clients";
    }


    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @RequestBody AppUser user) {
        if(userService.getUserById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/editUser/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        AppUser user = userService.getUserById(id).get();
        model.addAttribute("user", user);

        return "editUser"; // Nazwa widoku dla formularza edycji
    }

    @PostMapping("/editUser/save")
    public String saveEditedUser(@ModelAttribute("client") AppUser editedUser, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("client", editedUser);
            return "editUser";
        }
        userService.updateUser(editedUser);

        return "redirect:/clients";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser user) {
        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AppUser loginUser) {
        Optional<AppUser> user = userService.getUserByUsername(loginUser.getUsername());

        if (user.isPresent() && user.get().getPassword().equals(loginUser.getPassword())) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
