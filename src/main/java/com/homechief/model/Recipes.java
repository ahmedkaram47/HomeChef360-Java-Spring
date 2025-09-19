package com.homechief.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Recipes")
public class Recipes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "Cuisine")
    private String cuisine;

    @Column(name = "Category")
    private String category;

    @Column(name = "Difficulty")
    private String difficulty;

    @Column(name = "PrepTime")
    private Integer prepTime;

    @Column(name = "CookTime")
    private Integer cookTime;

    @Column(name = "Servings")
    private Integer servings;

    @Column(name = "CaloriesPerServing")
    private Double caloriesPerServing;

    @Column(name = "ProteinPerServing")
    private Double proteinPerServing;

    @Column(name = "CarbsPerServing")
    private Double carbsPerServing;

    @Column(name = "FatPerServing")
    private Double fatPerServing;

    @Column(name = "ImageUrl")
    private String imageUrl;

    @Column(name = "VideoUrl")
    private String videoUrl;

    @Column(name = "Rating")
    private Double rating = 0.00;

    @Column(name = "ReviewCount")
    private Integer reviewCount = 0;

    @Column(name = "ViewCount")
    private Integer viewCount = 0;

    @Column(name = "CreatedAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // One-to-many relationships (child entities will reference Recipes)
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredients> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeSteps> recipeSteps = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeVideos> recipeVideos = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeReviews> recipeReviews = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFavorites> userFavorites = new ArrayList<>();

    public Recipes() {}

    // Getters and setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Integer getPrepTime() { return prepTime; }
    public void setPrepTime(Integer prepTime) { this.prepTime = prepTime; }

    public Integer getCookTime() { return cookTime; }
    public void setCookTime(Integer cookTime) { this.cookTime = cookTime; }

    public Integer getServings() { return servings; }
    public void setServings(Integer servings) { this.servings = servings; }

    public Double getCaloriesPerServing() { return caloriesPerServing; }
    public void setCaloriesPerServing(Double caloriesPerServing) { this.caloriesPerServing = caloriesPerServing; }

    public Double getProteinPerServing() { return proteinPerServing; }
    public void setProteinPerServing(Double proteinPerServing) { this.proteinPerServing = proteinPerServing; }

    public Double getCarbsPerServing() { return carbsPerServing; }
    public void setCarbsPerServing(Double carbsPerServing) { this.carbsPerServing = carbsPerServing; }

    public Double getFatPerServing() { return fatPerServing; }
    public void setFatPerServing(Double fatPerServing) { this.fatPerServing = fatPerServing; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public List<RecipeIngredients> getRecipeIngredients() { return recipeIngredients; }
    public void setRecipeIngredients(List<RecipeIngredients> recipeIngredients) { this.recipeIngredients = recipeIngredients; }

    public List<RecipeSteps> getRecipeSteps() { return recipeSteps; }
    public void setRecipeSteps(List<RecipeSteps> recipeSteps) { this.recipeSteps = recipeSteps; }

    public List<RecipeVideos> getRecipeVideos() { return recipeVideos; }
    public void setRecipeVideos(List<RecipeVideos> recipeVideos) { this.recipeVideos = recipeVideos; }

    public List<RecipeReviews> getRecipeReviews() { return recipeReviews; }
    public void setRecipeReviews(List<RecipeReviews> recipeReviews) { this.recipeReviews = recipeReviews; }

    public List<UserFavorites> getUserFavorites() { return userFavorites; }
    public void setUserFavorites(List<UserFavorites> userFavorites) { this.userFavorites = userFavorites; }
}
