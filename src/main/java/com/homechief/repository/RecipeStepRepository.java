package com.homechief.repository;

import com.homechief.model.RecipeSteps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeStepRepository extends JpaRepository<RecipeSteps, Integer> {

    @Query("SELECT r FROM RecipeSteps r WHERE r.recipe.id = :recipeId ORDER BY r.stepNumber ASC")
    List<RecipeSteps> findByRecipeId(@Param("recipeId") Integer recipeId);
}
