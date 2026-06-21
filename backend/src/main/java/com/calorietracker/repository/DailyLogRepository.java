package com.calorietracker.repository;

import com.calorietracker.model.DailyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Integer> {
    Optional<DailyLog> findByUserIdAndLogDate(Integer userId, LocalDate logDate);
    List<DailyLog> findByUserIdOrderByLogDateDesc(Integer userId);
    List<DailyLog> findByUserIdAndLogDateBetween(Integer userId, LocalDate startDate, LocalDate endDate);
}
