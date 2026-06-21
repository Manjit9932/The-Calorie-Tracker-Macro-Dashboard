package com.calorietracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_entries", indexes = {
    @Index(name = "idx_log_id", columnList = "log_id"),
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_food_id", columnList = "food_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Integer entryId;

    @Column(name = "log_id", nullable = false)
    private Integer logId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "food_id", nullable = false)
    private Integer foodId;

    @Column(name = "portion_weight_grams", nullable = false)
    private BigDecimal portionWeightGrams;

    @Column(name = "calories", nullable = false)
    private BigDecimal calories;

    @Column(name = "protein_grams", nullable = false)
    private BigDecimal proteinGrams;

    @Column(name = "carbs_grams", nullable = false)
    private BigDecimal carbsGrams;

    @Column(name = "fats_grams", nullable = false)
    private BigDecimal fatsGrams;

    @Column(name = "meal_time")
    private LocalTime mealTime;

    @Column(name = "meal_type")
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (mealType == null) {
            mealType = MealType.SNACK;
        }
        if (mealTime == null) {
            mealTime = LocalTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
