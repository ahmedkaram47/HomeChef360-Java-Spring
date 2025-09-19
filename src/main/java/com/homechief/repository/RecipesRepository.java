package com.homechief.repository;

import com.homechief.model.Recipes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipesRepository extends JpaRepository<Recipes, Integer> {
    List<Recipes> findByNameContainingIgnoreCase(String name);
}
