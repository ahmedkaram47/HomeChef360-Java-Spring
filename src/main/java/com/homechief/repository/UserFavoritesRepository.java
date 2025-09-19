package com.homechief.repository;

import com.homechief.model.UserFavorites;
import com.homechief.model.User;
import com.homechief.model.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoritesRepository extends JpaRepository<UserFavorites, Integer> {
    List<UserFavorites> findByUser(User user);
    List<UserFavorites> findByRecipe(Recipes recipe);
}
