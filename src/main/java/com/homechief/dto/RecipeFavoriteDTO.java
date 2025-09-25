package com.homechief.dto;

import java.time.LocalDateTime;

public class RecipeFavoriteDTO {
    private Integer recipeId;
    private Integer userId;
    private LocalDateTime createdAt;

    // Getters & Setters

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
