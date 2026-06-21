package com.calorietracker.repository;

import com.calorietracker.model.FitnessGoal;
import com.calorietracker.model.FitnessGoalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FitnessGoalRepository extends JpaRepository<FitnessGoal, Integer> {
    Optional<FitnessGoal> findByUserIdAndGoalType(Integer userId, FitnessGoalType goalType);
    List<FitnessGoal> findByUserId(Integer userId);
}
