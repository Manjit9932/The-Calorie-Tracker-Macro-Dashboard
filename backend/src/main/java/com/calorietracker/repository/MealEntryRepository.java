package com.calorietracker.repository;

import com.calorietracker.model.MealEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MealEntryRepository extends JpaRepository<MealEntry, Integer> {
    List<MealEntry> findByLogIdOrderByMealTimeAsc(Integer logId);
    List<MealEntry> findByUserId(Integer userId);
    List<MealEntry> findByLogId(Integer logId);
}
