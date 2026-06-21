package com.calorietracker.controller;

import com.calorietracker.dto.DailyLogDTO;
import com.calorietracker.dto.MealEntryDTO;
import com.calorietracker.service.MealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class MealController {

    private final MealService mealService;

    /**
     * Add a new meal entry
     */
    @PostMapping("/add")
    public ResponseEntity<?> addMeal(
            @RequestParam Integer userId,
            @RequestParam Integer foodId,
            @RequestParam BigDecimal portionWeightGrams,
            @RequestParam(defaultValue = "SNACK") String mealType) {
        try {
            MealEntryDTO meal = mealService.addMeal(userId, foodId, portionWeightGrams, mealType);
            return ResponseEntity.status(HttpStatus.CREATED).body(meal);
        } catch (Exception e) {
            log.error("Error adding meal", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Add meal from simulated image upload
     */
    @PostMapping("/add-from-image")
    public ResponseEntity<?> addMealFromImage(
            @RequestParam Integer userId,
            @RequestParam String mockFoodName,
            @RequestParam BigDecimal mockPortionWeight) {
        try {
            MealEntryDTO meal = mealService.addMealFromImageUpload(userId, mockFoodName, mockPortionWeight);
            return ResponseEntity.status(HttpStatus.CREATED).body(meal);
        } catch (Exception e) {
            log.error("Error adding meal from image", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Delete a meal entry
     */
    @DeleteMapping("/{entryId}")
    public ResponseEntity<?> deleteMeal(@PathVariable Integer entryId) {
        try {
            mealService.deleteMeal(entryId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Meal deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting meal", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get today's daily log
     */
    @GetMapping("/today")
    public ResponseEntity<?> getTodayLog(@RequestParam Integer userId) {
        try {
            DailyLogDTO log = mealService.getTodayLog(userId);
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            log.error("Error fetching today's log", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get log for specific date
     */
    @GetMapping("/by-date")
    public ResponseEntity<?> getLogByDate(
            @RequestParam Integer userId,
            @RequestParam String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            DailyLogDTO log = mealService.getLogByDate(userId, parsedDate);
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            log.error("Error fetching log by date", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
