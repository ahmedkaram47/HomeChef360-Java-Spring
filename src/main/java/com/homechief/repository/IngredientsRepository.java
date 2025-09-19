package com.homechief.repository;

import com.homechief.model.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredients, Integer> {
    Optional<Ingredients> findByName(String name);
    List<Ingredients> findByCategory(String category);
}
