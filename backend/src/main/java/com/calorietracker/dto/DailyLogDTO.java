package com.calorietracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyLogDTO {
    private Integer logId;
    private Integer userId;
    private LocalDate logDate;
    private BigDecimal totalCalories;
    private BigDecimal totalProteinGrams;
    private BigDecimal totalCarbsGrams;
    private BigDecimal totalFatsGrams;
    private Boolean isBudgetExceeded;
    private Integer dailyCalorieTarget;
    private BigDecimal caloriePercentage;
    private List<MealEntryDTO> meals;
}
