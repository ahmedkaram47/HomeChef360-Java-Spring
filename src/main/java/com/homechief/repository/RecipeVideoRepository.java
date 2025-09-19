package com.homechief.repository;

import com.homechief.model.RecipeVideos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeVideoRepository extends JpaRepository<RecipeVideos, Integer> {
    List<RecipeVideos> findByRecipeId(Integer recipeId);
}
