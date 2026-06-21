package com.calorietracker.service;

import com.calorietracker.dto.DailyLogDTO;
import com.calorietracker.dto.MealEntryDTO;
import com.calorietracker.model.*;
import com.calorietracker.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MealService {

    private final MealEntryRepository mealEntryRepository;
    private final DailyLogRepository dailyLogRepository;
    private final UserRepository userRepository;
    private final FitnessGoalRepository fitnessGoalRepository;
    private final FoodDatabaseRepository foodDatabaseRepository;

    /**
     * Nutrient Scaling Algorithm:
     * Calculates exact calories, protein, carbs, and fats based on portion weight
     */
    public MealEntryDTO addMeal(Integer userId, Integer foodId, BigDecimal portionWeightGrams, String mealType) {
        // Get food from database
        FoodDatabase food = foodDatabaseRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        // Get or create daily log for today
        LocalDate today = LocalDate.now();
        DailyLog dailyLog = dailyLogRepository.findByUserIdAndLogDate(userId, today)
                .orElseGet(() -> createNewDailyLog(userId, today));

        // Scale nutrients based on portion weight (convert from 100g baseline)
        BigDecimal weightRatio = portionWeightGrams.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        BigDecimal scaledCalories = food.getCaloriesPer100g().multiply(weightRatio).setScale(2, RoundingMode.HALF_UP);
        BigDecimal scaledProtein = food.getProteinPer100g().multiply(weightRatio).setScale(2, RoundingMode.HALF_UP);
        BigDecimal scaledCarbs = food.getCarbsPer100g().multiply(weightRatio).setScale(2, RoundingMode.HALF_UP);
        BigDecimal scaledFats = food.getFatsPer100g().multiply(weightRatio).setScale(2, RoundingMode.HALF_UP);

        // Create meal entry
        MealEntry mealEntry = MealEntry.builder()
                .logId(dailyLog.getLogId())
                .userId(userId)
                .foodId(foodId)
                .portionWeightGrams(portionWeightGrams)
                .calories(scaledCalories)
                .proteinGrams(scaledProtein)
                .carbsGrams(scaledCarbs)
                .fatsGrams(scaledFats)
                .mealTime(LocalTime.now())
                .mealType(MealType.valueOf(mealType))
                .build();

        MealEntry savedEntry = mealEntryRepository.save(mealEntry);

        // Update daily log totals
        updateDailyLogTotals(dailyLog);

        log.info("Meal added for user: {} with food: {}", userId, food.getFoodName());

        return convertToMealEntryDTO(savedEntry, food.getFoodName());
    }

    /**
     * Add mock meal from simulated image upload
     */
    public MealEntryDTO addMealFromImageUpload(Integer userId, String mockFoodName, BigDecimal mockPortionWeight) {
        // Find or create mock food entry
        FoodDatabase food = foodDatabaseRepository.findByFoodName(mockFoodName)
                .orElseThrow(() -> new RuntimeException("Mock food not found"));

        return addMeal(userId, food.getFoodId(), mockPortionWeight, "SNACK");
    }

    /**
     * Delete a meal entry and update totals
     */
    public void deleteMeal(Integer entryId) {
        MealEntry mealEntry = mealEntryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Meal entry not found"));

        Integer logId = mealEntry.getLogId();
        mealEntryRepository.deleteById(entryId);

        // Update daily log totals
        DailyLog dailyLog = dailyLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Daily log not found"));

        updateDailyLogTotals(dailyLog);

        log.info("Meal deleted: {}", entryId);
    }

    /**
     * Get today's log with all meal entries
     */
    public DailyLogDTO getTodayLog(Integer userId) {
        LocalDate today = LocalDate.now();
        DailyLog dailyLog = dailyLogRepository.findByUserIdAndLogDate(userId, today)
                .orElseGet(() -> createNewDailyLog(userId, today));

        return convertToDailyLogDTO(dailyLog, userId);
    }

    /**
     * Get specific date log
     */
    public DailyLogDTO getLogByDate(Integer userId, LocalDate date) {
        DailyLog dailyLog = dailyLogRepository.findByUserIdAndLogDate(userId, date)
                .orElseThrow(() -> new RuntimeException("No log found for this date"));

        return convertToDailyLogDTO(dailyLog, userId);
    }

    /**
     * Update daily log totals from all meal entries
     */
    private void updateDailyLogTotals(DailyLog dailyLog) {
        List<MealEntry> meals = mealEntryRepository.findByLogId(dailyLog.getLogId());

        BigDecimal totalCalories = BigDecimal.ZERO;
        BigDecimal totalProtein = BigDecimal.ZERO;
        BigDecimal totalCarbs = BigDecimal.ZERO;
        BigDecimal totalFats = BigDecimal.ZERO;

        for (MealEntry meal : meals) {
            totalCalories = totalCalories.add(meal.getCalories());
            totalProtein = totalProtein.add(meal.getProteinGrams());
            totalCarbs = totalCarbs.add(meal.getCarbsGrams());
            totalFats = totalFats.add(meal.getFatsGrams());
        }

        // Get user's current daily calorie target
        User user = userRepository.findById(dailyLog.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Integer dailyTarget = user.getDailyCalorieTarget();

        // Check if budget exceeded
        boolean isBudgetExceeded = totalCalories.compareTo(new BigDecimal(dailyTarget)) > 0;

        dailyLog.setTotalCalories(totalCalories);
        dailyLog.setTotalProteinGrams(totalProtein);
        dailyLog.setTotalCarbsGrams(totalCarbs);
        dailyLog.setTotalFatsGrams(totalFats);
        dailyLog.setIsBudgetExceeded(isBudgetExceeded);

        dailyLogRepository.save(dailyLog);

        log.info("Daily log updated for user {} on {}: Calories: {}, Budget Exceeded: {}",
                dailyLog.getUserId(), dailyLog.getLogDate(), totalCalories, isBudgetExceeded);
    }

    /**
     * Create new daily log for user
     */
    private DailyLog createNewDailyLog(Integer userId, LocalDate date) {
        DailyLog dailyLog = DailyLog.builder()
                .userId(userId)
                .logDate(date)
                .totalCalories(BigDecimal.ZERO)
                .totalProteinGrams(BigDecimal.ZERO)
                .totalCarbsGrams(BigDecimal.ZERO)
                .totalFatsGrams(BigDecimal.ZERO)
                .isBudgetExceeded(false)
                .build();

        return dailyLogRepository.save(dailyLog);
    }

    /**
     * Convert MealEntry to DTO
     */
    private MealEntryDTO convertToMealEntryDTO(MealEntry meal, String foodName) {
        return MealEntryDTO.builder()
                .entryId(meal.getEntryId())
                .logId(meal.getLogId())
                .userId(meal.getUserId())
                .foodId(meal.getFoodId())
                .foodName(foodName)
                .portionWeightGrams(meal.getPortionWeightGrams())
                .calories(meal.getCalories())
                .proteinGrams(meal.getProteinGrams())
                .carbsGrams(meal.getCarbsGrams())
                .fatsGrams(meal.getFatsGrams())
                .mealTime(meal.getMealTime() != null ? meal.getMealTime().toString() : "")
                .mealType(meal.getMealType().toString())
                .build();
    }

    /**
     * Convert DailyLog to DTO with meals
     */
    private DailyLogDTO convertToDailyLogDTO(DailyLog dailyLog, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<MealEntry> meals = mealEntryRepository.findByLogIdOrderByMealTimeAsc(dailyLog.getLogId());
        List<MealEntryDTO> mealDTOs = meals.stream()
                .map(meal -> {
                    FoodDatabase food = foodDatabaseRepository.findById(meal.getFoodId()).orElse(null);
                    return convertToMealEntryDTO(meal, food != null ? food.getFoodName() : "Unknown");
                })
                .collect(Collectors.toList());

        BigDecimal caloriePercentage = dailyLog.getTotalCalories()
                .divide(new BigDecimal(user.getDailyCalorieTarget()), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return DailyLogDTO.builder()
                .logId(dailyLog.getLogId())
                .userId(dailyLog.getUserId())
                .logDate(dailyLog.getLogDate())
                .totalCalories(dailyLog.getTotalCalories())
                .totalProteinGrams(dailyLog.getTotalProteinGrams())
                .totalCarbsGrams(dailyLog.getTotalCarbsGrams())
                .totalFatsGrams(dailyLog.getTotalFatsGrams())
                .isBudgetExceeded(dailyLog.getIsBudgetExceeded())
                .dailyCalorieTarget(user.getDailyCalorieTarget())
                .caloriePercentage(caloriePercentage)
                .meals(mealDTOs)
                .build();
    }
}
