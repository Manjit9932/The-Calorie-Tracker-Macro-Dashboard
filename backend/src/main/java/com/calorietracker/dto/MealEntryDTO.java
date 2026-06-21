package com.calorietracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealEntryDTO {
    private Integer entryId;
    private Integer logId;
    private Integer userId;
    private Integer foodId;
    private String foodName;
    private BigDecimal portionWeightGrams;
    private BigDecimal calories;
    private BigDecimal proteinGrams;
    private BigDecimal carbsGrams;
    private BigDecimal fatsGrams;
    private String mealTime;
    private String mealType;
}
