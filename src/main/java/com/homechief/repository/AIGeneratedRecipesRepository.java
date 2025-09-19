package com.homechief.repository;

import com.homechief.model.AIGeneratedRecipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIGeneratedRecipesRepository extends JpaRepository<AIGeneratedRecipes, Integer> {
}
