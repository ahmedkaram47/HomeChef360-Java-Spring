package com.homechief.repository;

import com.homechief.model.AIGeneratedRecipes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIGeneratedRecipesRepository extends JpaRepository<AIGeneratedRecipes, Integer> {
    List<AIGeneratedRecipes> findByUserId(Integer userId);
}
