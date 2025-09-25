package com.homechief.repository;

import com.homechief.model.UserFavorites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFavoritesRepository extends JpaRepository<UserFavorites, Integer> {
    List<UserFavorites> findByUserId(Integer userId);
    Optional<UserFavorites> findByUserIdAndRecipeId(Integer userId, Integer recipeId);
}
