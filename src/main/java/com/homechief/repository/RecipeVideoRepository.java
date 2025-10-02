package com.homechief.repository;

import com.homechief.model.RecipeVideos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeVideoRepository extends JpaRepository<RecipeVideos, Integer> {
    @Query("SELECT v FROM RecipeVideos v WHERE v.recipe.id = :recipeId")
    List<RecipeVideos> findByRecipeId(@Param("recipeId") Integer recipeId);
}
