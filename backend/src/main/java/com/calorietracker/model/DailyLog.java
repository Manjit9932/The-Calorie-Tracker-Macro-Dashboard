package com.calorietracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_logs", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_log_date", columnList = "log_date")
}, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "log_date"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "total_calories")
    private BigDecimal totalCalories;

    @Column(name = "total_protein_grams")
    private BigDecimal totalProteinGrams;

    @Column(name = "total_carbs_grams")
    private BigDecimal totalCarbsGrams;

    @Column(name = "total_fats_grams")
    private BigDecimal totalFatsGrams;

    @Column(name = "is_budget_exceeded")
    private Boolean isBudgetExceeded;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (totalCalories == null) {
            totalCalories = BigDecimal.ZERO;
        }
        if (totalProteinGrams == null) {
            totalProteinGrams = BigDecimal.ZERO;
        }
        if (totalCarbsGrams == null) {
            totalCarbsGrams = BigDecimal.ZERO;
        }
        if (totalFatsGrams == null) {
            totalFatsGrams = BigDecimal.ZERO;
        }
        if (isBudgetExceeded == null) {
            isBudgetExceeded = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
