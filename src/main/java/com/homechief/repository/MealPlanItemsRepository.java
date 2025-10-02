package com.homechief.repository;

import com.homechief.model.MealPlanItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealPlanItemsRepository extends JpaRepository<MealPlanItems, Integer> {

    @Query("SELECT i FROM MealPlanItems i WHERE i.mealPlan.id = :mealPlanId")
    List<MealPlanItems> findByMealPlanId(@Param("mealPlanId") Integer mealPlanId);
}
