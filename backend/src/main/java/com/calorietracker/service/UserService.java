package com.calorietracker.service;

import com.calorietracker.model.FitnessGoal;
import com.calorietracker.model.FitnessGoalType;
import com.calorietracker.model.User;
import com.calorietracker.repository.FitnessGoalRepository;
import com.calorietracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final FitnessGoalRepository fitnessGoalRepository;

    /**
     * Create a new user with default fitness goal
     */
    public User createUser(String username, String email, String password) {
        // Check if user already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Create user with default maintenance goal (2000 calories)
        User user = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .dailyCalorieTarget(2000)
                .currentFitnessGoal(FitnessGoalType.MAINTENANCE)
                .build();

        User savedUser = userRepository.save(user);

        // Create default fitness goals for the user
        createDefaultFitnessGoals(savedUser.getUserId());

        log.info("User created: {}", username);
        return savedUser;
    }

    /**
     * Get user by ID
     */
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Login user with username and password
     */
    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid username or password");
        }
        
        log.info("User logged in: {}", username);
        return user;
    }

    /**
     * Update fitness goal and daily calorie target
     */
    public User updateFitnessGoal(Integer userId, FitnessGoalType newGoal) {
        User user = getUserById(userId);

        // Get the fitness goal configuration
        FitnessGoal goalConfig = fitnessGoalRepository.findByUserIdAndGoalType(userId, newGoal)
                .orElseThrow(() -> new RuntimeException("Fitness goal configuration not found"));

        // Update user's current goal and target
        user.setCurrentFitnessGoal(newGoal);
        user.setDailyCalorieTarget(goalConfig.getDailyCalorieTarget());

        User updatedUser = userRepository.save(user);
        log.info("Fitness goal updated for user {}: {}", userId, newGoal);

        return updatedUser;
    }

    /**
     * Get all fitness goals for user
     */
    public List<FitnessGoal> getUserFitnessGoals(Integer userId) {
        return fitnessGoalRepository.findByUserId(userId);
    }

    /**
     * Create default fitness goals for a new user
     * Based on Mifflin-St Jeor calculation for TDEE
     */
    private void createDefaultFitnessGoals(Integer userId) {
        // Standard TDEE = 2000 (placeholder, typically calculated from age, weight, height, activity level)
        int tdee = 2500;

        // Weight Loss: 20% deficit
        FitnessGoal weightLoss = FitnessGoal.builder()
                .userId(userId)
                .goalType(FitnessGoalType.WEIGHT_LOSS)
                .dailyCalorieTarget(Math.round(tdee * 0.8f))  // 2000 calories
                .dailyProteinTargetGrams(new BigDecimal("150"))  // High protein for satiety
                .dailyCarbsTargetGrams(new BigDecimal("150"))
                .dailyFatsTargetGrams(new BigDecimal("50"))
                .description("Weight Loss: 20% calorie deficit with high protein")
                .build();

        // Maintenance: 1.0x TDEE
        FitnessGoal maintenance = FitnessGoal.builder()
                .userId(userId)
                .goalType(FitnessGoalType.MAINTENANCE)
                .dailyCalorieTarget(2000)  // Standard 2000 maintenance
                .dailyProteinTargetGrams(new BigDecimal("140"))
                .dailyCarbsTargetGrams(new BigDecimal("280"))
                .dailyFatsTargetGrams(new BigDecimal("80"))
                .description("Maintenance: Balanced macros for steady state")
                .build();

        // Muscle Gain: 20% surplus
        FitnessGoal muscleGain = FitnessGoal.builder()
                .userId(userId)
                .goalType(FitnessGoalType.MUSCLE_GAIN)
                .dailyCalorieTarget(Math.round(tdee * 1.2f))  // 3000 calories
                .dailyProteinTargetGrams(new BigDecimal("180"))  // High protein for muscle synthesis
                .dailyCarbsTargetGrams(new BigDecimal("320"))
                .dailyFatsTargetGrams(new BigDecimal("100"))
                .description("Muscle Gain: 20% calorie surplus with high protein")
                .build();

        fitnessGoalRepository.saveAll(List.of(weightLoss, maintenance, muscleGain));
        log.info("Default fitness goals created for user: {}", userId);
    }
}
