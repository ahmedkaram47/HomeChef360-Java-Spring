package com.homechief.repository;

import com.homechief.model.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipesRepository extends JpaRepository<Recipes, Integer> {
    List<Recipes> findByNameContainingIgnoreCase(String name);
    @Query("SELECT r FROM Recipes r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Recipes> searchRecipes(@Param("q") String q);

}
