package com.calorietracker.repository;

import com.calorietracker.model.FoodDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodDatabaseRepository extends JpaRepository<FoodDatabase, Integer> {
    Optional<FoodDatabase> findByFoodName(String foodName);
    List<FoodDatabase> findByFoodCategoryAndIsActiveTrue(String foodCategory);
    List<FoodDatabase> findByIsActiveTrue();
    List<FoodDatabase> findByFoodNameContainingIgnoreCase(String foodName);
}
