package com.homechief.controller;

import com.homechief.dto.IngredientDTO;
import com.homechief.dto.UserPantryDTO;
import com.homechief.dto.UserPantryRequestDTO;
import com.homechief.model.Ingredients;
import com.homechief.model.User;
import com.homechief.model.UserPantry;
import com.homechief.repository.IngredientsRepository;
import com.homechief.repository.UserPantryRepository;
import com.homechief.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ingredients")
public class IngredientsController {

    private final IngredientsRepository ingredientsRepository;

    public IngredientsController(IngredientsRepository ingredientsRepository) {
        this.ingredientsRepository = ingredientsRepository;
    }

    // 1. Get all ingredients
    @GetMapping("/all")
    public ResponseEntity<?> getAllIngredients() {
        return ResponseEntity.ok(ingredientsRepository.findAll());
    }

    // 2. Add new ingredient to catalog
    @PostMapping("/add")
    public ResponseEntity<?> addIngredient(@RequestBody Ingredients ingredient) {
        if (ingredientsRepository.findByName(ingredient.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Ingredient already exists");
        }
        ingredient.setCreatedAt(LocalDateTime.now());
        ingredient.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(ingredientsRepository.save(ingredient));
    }

    // 3. Update ingredient in catalog
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateIngredient(@PathVariable Integer id, @RequestBody Ingredients updated) {
        Ingredients ingredient = ingredientsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        ingredient.setName(updated.getName());
        ingredient.setCategory(updated.getCategory());
        ingredient.setUnit(updated.getUnit());
        ingredient.setCaloriesPerUnit(updated.getCaloriesPerUnit());
        ingredient.setProteinPerUnit(updated.getProteinPerUnit());
        ingredient.setCarbsPerUnit(updated.getCarbsPerUnit());
        ingredient.setFatPerUnit(updated.getFatPerUnit());
        ingredient.setFiberPerUnit(updated.getFiberPerUnit());
        ingredient.setSugarPerUnit(updated.getSugarPerUnit());
        ingredient.setSodiumPerUnit(updated.getSodiumPerUnit());
        ingredient.setImageUrl(updated.getImageUrl());
        ingredient.setDescription(updated.getDescription());
        ingredient.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok(ingredientsRepository.save(ingredient));
    }

    // 4. Delete ingredient
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable Integer id) {
        Ingredients ingredient = ingredientsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
        ingredientsRepository.delete(ingredient);
        return ResponseEntity.ok("Ingredient deleted successfully");
    }

    // 5. Search ingredients
    @GetMapping("/search")
    public ResponseEntity<?> searchIngredients(@RequestParam("q") String query) {
        return ResponseEntity.ok(ingredientsRepository.findByNameContainingIgnoreCase(query));
    }
}
