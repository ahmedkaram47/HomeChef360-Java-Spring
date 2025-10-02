package com.homechief.repository;

import com.homechief.model.MealPlans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealPlansRepository extends JpaRepository<MealPlans, Integer> {

    @Query("SELECT m FROM MealPlans m WHERE m.user.id = :userId")
    List<MealPlans> findByUserId(@Param("userId") Integer userId);
}
