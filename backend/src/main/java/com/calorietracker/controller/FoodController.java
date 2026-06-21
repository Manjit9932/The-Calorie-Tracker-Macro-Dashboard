package com.calorietracker.controller;

import com.calorietracker.model.FoodDatabase;
import com.calorietracker.repository.FoodDatabaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class FoodController {

    private final FoodDatabaseRepository foodDatabaseRepository;

    /**
     * Get all active foods
     */
    @GetMapping
    public ResponseEntity<?> getAllFoods() {
        try {
            List<FoodDatabase> foods = foodDatabaseRepository.findByIsActiveTrue();
            return ResponseEntity.ok(foods);
        } catch (Exception e) {
            log.error("Error fetching foods", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get food by ID
     */
    @GetMapping("/{foodId}")
    public ResponseEntity<?> getFoodById(@PathVariable Integer foodId) {
        try {
            FoodDatabase food = foodDatabaseRepository.findById(foodId)
                    .orElseThrow(() -> new RuntimeException("Food not found"));
            return ResponseEntity.ok(food);
        } catch (Exception e) {
            log.error("Error fetching food", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Search foods by name
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchFoods(@RequestParam String name) {
        try {
            List<FoodDatabase> foods = foodDatabaseRepository.findByFoodNameContainingIgnoreCase(name);
            return ResponseEntity.ok(foods);
        } catch (Exception e) {
            log.error("Error searching foods", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get foods by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getFoodsByCategory(@PathVariable String category) {
        try {
            List<FoodDatabase> foods = foodDatabaseRepository.findByFoodCategoryAndIsActiveTrue(category);
            return ResponseEntity.ok(foods);
        } catch (Exception e) {
            log.error("Error fetching foods by category", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
