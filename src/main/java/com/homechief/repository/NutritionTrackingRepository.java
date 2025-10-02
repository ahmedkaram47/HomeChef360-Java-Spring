package com.homechief.repository;

import com.homechief.model.NutritionTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NutritionTrackingRepository extends JpaRepository<NutritionTracking, Integer> {
    List<NutritionTracking> findByUserId(Integer userId);
    List<NutritionTracking> findByUserIdAndDate(Integer userId, LocalDate date);
}
