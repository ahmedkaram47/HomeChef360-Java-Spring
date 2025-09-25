package com.homechief.dto;

import java.time.LocalDateTime;

public class UserFavoritesDTO {
    private Integer id;
    private Integer recipeId;
    private String recipeName;
    private String recipeImageUrl;
    private LocalDateTime createdAt;

    public UserFavoritesDTO() {}

    // getters & setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getRecipeId() { return recipeId; }
    public void setRecipeId(Integer recipeId) { this.recipeId = recipeId; }

    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }

    public String getRecipeImageUrl() { return recipeImageUrl; }
    public void setRecipeImageUrl(String recipeImageUrl) { this.recipeImageUrl = recipeImageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
