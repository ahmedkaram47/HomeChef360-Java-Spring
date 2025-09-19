package com.homechief.repository;

import com.homechief.model.MealPlans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealPlansRepository extends JpaRepository<MealPlans, Integer> {
}
