package com.calorietracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_goals", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id")
}, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "goal_type"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FitnessGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Integer goalId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "goal_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FitnessGoalType goalType;

    @Column(name = "daily_calorie_target", nullable = false)
    private Integer dailyCalorieTarget;

    @Column(name = "daily_protein_target_grams", nullable = false)
    private BigDecimal dailyProteinTargetGrams;

    @Column(name = "daily_carbs_target_grams", nullable = false)
    private BigDecimal dailyCarbsTargetGrams;

    @Column(name = "daily_fats_target_grams", nullable = false)
    private BigDecimal dailyFatsTargetGrams;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
