package com.homechief.repository;

import com.homechief.model.MealPlanItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealPlanItemsRepository extends JpaRepository<MealPlanItems, Integer> {
}
