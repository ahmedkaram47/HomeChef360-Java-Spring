package com.homechief.repository;

import com.homechief.model.RecipeSteps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeSteps, Integer> {
    List<RecipeSteps> findByRecipeId(Integer recipeId);
}
