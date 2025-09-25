package com.homechief.repository;

import com.homechief.model.RecipeIngredients;
import com.homechief.model.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredients, Integer> {
    List<RecipeIngredients> findByRecipe(Recipes recipe);
    List<RecipeIngredients> findByRecipeId(Integer recipeId);
}
