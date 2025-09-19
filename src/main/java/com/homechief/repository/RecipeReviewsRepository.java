package com.homechief.repository;

import com.homechief.model.RecipeReviews;
import com.homechief.model.Recipes;
import com.homechief.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeReviewsRepository extends JpaRepository<RecipeReviews, Integer> {
    List<RecipeReviews> findByRecipe(Recipes recipe);
    List<RecipeReviews> findByUser(User user);
}
