package com.homechief.controller;

import com.homechief.dto.*;
import com.homechief.model.*;
import com.homechief.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipes")
public class RecipesController {

    private final RecipesRepository recipesRepository;
    private final UserRepository userRepository;
    private final UserFavoritesRepository userFavoritesRepository;
    private final RecipeReviewsRepository recipeReviewsRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;

    public RecipesController(RecipesRepository recipesRepository,
                             UserRepository userRepository,
                             UserFavoritesRepository userFavoritesRepository,
                             RecipeReviewsRepository recipeReviewsRepository,
                             RecipeIngredientsRepository recipeIngredientsRepository) {
        this.recipesRepository = recipesRepository;
        this.userRepository = userRepository;
        this.userFavoritesRepository = userFavoritesRepository;
        this.recipeReviewsRepository = recipeReviewsRepository;
        this.recipeIngredientsRepository = recipeIngredientsRepository;
    }

    // --- GET ALL RECIPES ---
    @GetMapping("/all")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        List<RecipeDTO> recipes = recipesRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(recipes);
    }

    // --- GET RECIPE BY ID ---
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Integer id) {
        return recipesRepository.findById(id)
                .map(recipe -> ResponseEntity.ok(mapToDTO(recipe)))
                .orElse(ResponseEntity.notFound().build());
    }

    // --- CREATE RECIPE ---
    @PostMapping("/add")
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeRequestDTO req) {
        Recipes recipe = new Recipes();
        mapRequestToEntity(req, recipe);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(mapToDTO(recipesRepository.save(recipe)));
    }

    // --- UPDATE RECIPE ---
    @PutMapping("/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Integer id,
                                                  @RequestBody RecipeRequestDTO req) {
        return recipesRepository.findById(id).map(recipe -> {
            mapRequestToEntity(req, recipe);
            recipe.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(mapToDTO(recipesRepository.save(recipe)));
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- DELETE RECIPE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Integer id) {
        return recipesRepository.findById(id).map(recipe -> {
            recipesRepository.delete(recipe);
            return ResponseEntity.ok("Recipe deleted");
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- SEARCH ---
    @GetMapping("/search")
    public ResponseEntity<List<RecipeDTO>> searchRecipes(@RequestParam String q) {
        List<RecipeDTO> results = recipesRepository.searchRecipes(q).stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(results);
    }


    // --- GET CATEGORIES ---
    @GetMapping("/categories")
    public ResponseEntity<Set<String>> getCategories() {
        Set<String> categories = recipesRepository.findAll().stream()
                .map(Recipes::getCategory)
                .filter(c -> c != null && !c.isEmpty())
                .collect(Collectors.toSet());
        return ResponseEntity.ok(categories);
    }

    // --- GET CUISINES ---
    @GetMapping("/cuisines")
    public ResponseEntity<Set<String>> getCuisines() {
        Set<String> cuisines = recipesRepository.findAll().stream()
                .map(Recipes::getCuisine)
                .filter(c -> c != null && !c.isEmpty())
                .collect(Collectors.toSet());
        return ResponseEntity.ok(cuisines);
    }

    // --- FAVORITES ---
    @PostMapping("/{id}/favorite")
    public ResponseEntity<RecipeFavoriteDTO> addFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable Integer id) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Recipes recipe = recipesRepository.findById(id).orElseThrow();

        if (userFavoritesRepository.findByUserIdAndRecipeId(user.getId(), recipe.getId()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        UserFavorites fav = new UserFavorites();
        fav.setUser(user);
        fav.setRecipe(recipe);
        fav.setCreatedAt(LocalDateTime.now());

        UserFavorites saved = userFavoritesRepository.save(fav);

        RecipeFavoriteDTO dto = new RecipeFavoriteDTO();
        dto.setRecipeId(recipe.getId());
        dto.setUserId(user.getId());
        dto.setCreatedAt(saved.getCreatedAt());

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<String> removeFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable Integer id) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        UserFavorites fav = userFavoritesRepository.findByUserIdAndRecipeId(user.getId(), id)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        userFavoritesRepository.delete(fav);
        return ResponseEntity.ok("Favorite removed");
    }

    // --- REVIEWS ---
    @PostMapping("/{id}/review")
    public ResponseEntity<RecipeReviewDTO> addReview(@AuthenticationPrincipal UserDetails userDetails,
                                                     @PathVariable Integer id,
                                                     @RequestBody RecipeReviewDTO reviewDto) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Recipes recipe = recipesRepository.findById(id).orElseThrow();

        RecipeReviews review = new RecipeReviews();
        review.setUser(user);
        review.setRecipe(recipe);
        review.setRating(reviewDto.getRating());
        review.setReviewText(reviewDto.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        RecipeReviews saved = recipeReviewsRepository.save(review);

        RecipeReviewDTO dto = new RecipeReviewDTO();
        dto.setId(saved.getId());
        dto.setReviewerName(user.getName());
        dto.setRating(saved.getRating());
        dto.setComment(saved.getReviewText());
        dto.setCreatedAt(saved.getCreatedAt());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<RecipeReviewDTO>> getReviews(@PathVariable Integer id) {
        List<RecipeReviewDTO> reviews = recipeReviewsRepository.findByRecipeId(id).stream()
                .map(r -> {
                    RecipeReviewDTO dto = new RecipeReviewDTO();
                    dto.setId(r.getId());
                    dto.setReviewerName(r.getUser().getName());
                    dto.setRating(r.getRating());
                    dto.setComment(r.getReviewText());
                    dto.setCreatedAt(r.getCreatedAt());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(reviews);
    }

    // --- INGREDIENTS ---
    @GetMapping("/{id}/ingredients")
    public ResponseEntity<List<RecipeIngredientDTO>> getRecipeIngredients(@PathVariable Integer id) {
        List<RecipeIngredientDTO> ingredients = recipeIngredientsRepository.findByRecipeId(id).stream()
                .map(ri -> {
                    RecipeIngredientDTO dto = new RecipeIngredientDTO();
                    dto.setIngredientId(ri.getId());         // RecipeIngredients.ID
                    dto.setIngredientName(ri.getIngredientName());
                    dto.setQuantity(ri.getQuantity());       // String
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping("/{id}/ingredients")
    public ResponseEntity<RecipeIngredientDTO> addIngredientToRecipe(@PathVariable Integer id,
                                                                     @RequestBody RecipeIngredientDTO dto) {
        Recipes recipe = recipesRepository.findById(id).orElseThrow();

        RecipeIngredients ingredient = new RecipeIngredients();
        ingredient.setRecipe(recipe);
        ingredient.setIngredientName(dto.getIngredientName());
        ingredient.setQuantity(dto.getQuantity());  // String
        ingredient.setUnit(dto.getUnit());          // String

        RecipeIngredients saved = recipeIngredientsRepository.save(ingredient);

        RecipeIngredientDTO response = new RecipeIngredientDTO();
        response.setIngredientId(saved.getId());
        response.setIngredientName(saved.getIngredientName());
        response.setQuantity(saved.getQuantity());
        response.setUnit(saved.getUnit());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{recipeId}/ingredients/{ingredientId}")
    public ResponseEntity<RecipeIngredientDTO> updateIngredientInRecipe(@PathVariable Integer recipeId,
                                                                        @PathVariable Integer ingredientId,
                                                                        @RequestBody RecipeIngredientDTO dto) {
        return recipeIngredientsRepository.findById(ingredientId).map(ingredient -> {
            ingredient.setIngredientName(dto.getIngredientName());
            ingredient.setQuantity(dto.getQuantity());
            ingredient.setUnit(dto.getUnit());
            RecipeIngredients updated = recipeIngredientsRepository.save(ingredient);

            RecipeIngredientDTO response = new RecipeIngredientDTO();
            response.setIngredientId(updated.getId());
            response.setIngredientName(updated.getIngredientName());
            response.setQuantity(updated.getQuantity());
            response.setUnit(updated.getUnit());

            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{recipeId}/ingredients/{ingredientId}")
    public ResponseEntity<String> removeIngredientFromRecipe(@PathVariable Integer recipeId,
                                                             @PathVariable Integer ingredientId) {
        return recipeIngredientsRepository.findById(ingredientId).map(ingredient -> {
            recipeIngredientsRepository.delete(ingredient);
            return ResponseEntity.ok("Ingredient removed");
        }).orElse(ResponseEntity.notFound().build());
    }

    // ----------------- PRIVATE MAPPERS -----------------
    private RecipeDTO mapToDTO(Recipes recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setCuisine(recipe.getCuisine());
        dto.setCategory(recipe.getCategory());
        dto.setDifficulty(recipe.getDifficulty());
        dto.setPrepTime(recipe.getPrepTime());
        dto.setCookTime(recipe.getCookTime());
        dto.setServings(recipe.getServings());
        dto.setCaloriesPerServing(recipe.getCaloriesPerServing());
        dto.setProteinPerServing(recipe.getProteinPerServing());
        dto.setCarbsPerServing(recipe.getCarbsPerServing());
        dto.setFatPerServing(recipe.getFatPerServing());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setVideoUrl(recipe.getVideoUrl());
        dto.setCreatedAt(recipe.getCreatedAt());
        dto.setUpdatedAt(recipe.getUpdatedAt());
        return dto;
    }

    private void mapRequestToEntity(RecipeRequestDTO req, Recipes recipe) {
        recipe.setName(req.getName());
        recipe.setDescription(req.getDescription());
        recipe.setCuisine(req.getCuisine());
        recipe.setCategory(req.getCategory());
        recipe.setDifficulty(req.getDifficulty());
        recipe.setPrepTime(req.getPrepTime());
        recipe.setCookTime(req.getCookTime());
        recipe.setServings(req.getServings());
        recipe.setCaloriesPerServing(req.getCaloriesPerServing());
        recipe.setProteinPerServing(req.getProteinPerServing());
        recipe.setCarbsPerServing(req.getCarbsPerServing());
        recipe.setFatPerServing(req.getFatPerServing());
        recipe.setImageUrl(req.getImageUrl());
        recipe.setVideoUrl(req.getVideoUrl());
    }
}
