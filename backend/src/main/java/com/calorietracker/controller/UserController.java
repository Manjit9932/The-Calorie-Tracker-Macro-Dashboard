package com.calorietracker.controller;

import com.calorietracker.model.FitnessGoal;
import com.calorietracker.model.FitnessGoalType;
import com.calorietracker.model.User;
import com.calorietracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class UserController {

    private final UserService userService;

    /**
     * Create a new user
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {
        try {
            User user = userService.createUser(username, email, password);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            log.error("Error creating user", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Login user with username and password
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestParam String username,
            @RequestParam String password) {
        try {
            User user = userService.loginUser(username, password);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Login error", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Integer userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error fetching user", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update fitness goal
     */
    @PutMapping("/{userId}/fitness-goal")
    public ResponseEntity<?> updateFitnessGoal(
            @PathVariable Integer userId,
            @RequestParam String goalType) {
        try {
            FitnessGoalType goal = FitnessGoalType.valueOf(goalType);
            User user = userService.updateFitnessGoal(userId, goal);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error updating fitness goal", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get all fitness goals for user
     */
    @GetMapping("/{userId}/fitness-goals")
    public ResponseEntity<?> getUserFitnessGoals(@PathVariable Integer userId) {
        try {
            List<FitnessGoal> goals = userService.getUserFitnessGoals(userId);
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            log.error("Error fetching fitness goals", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
