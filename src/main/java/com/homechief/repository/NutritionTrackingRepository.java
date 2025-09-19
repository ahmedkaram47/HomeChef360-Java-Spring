package com.homechief.repository;

import com.homechief.model.NutritionTracking;
import com.homechief.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NutritionTrackingRepository extends JpaRepository<NutritionTracking, Integer> {
    List<NutritionTracking> findByUserAndDate(User user, LocalDate date);
    List<NutritionTracking> findByUser(User user);
}
