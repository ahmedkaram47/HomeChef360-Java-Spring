package com.homechief.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserPreferencesDTO {
    private Integer id;
    private String dietaryType;
    private List<String> allergies;
    private List<String> dislikes;
    private Integer servings;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserPreferencesDTO() {}

    // getters & setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDietaryType() { return dietaryType; }
    public void setDietaryType(String dietaryType) { this.dietaryType = dietaryType; }

    public List<String> getAllergies() { return allergies; }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }

    public List<String> getDislikes() { return dislikes; }
    public void setDislikes(List<String> dislikes) { this.dislikes = dislikes; }

    public Integer getServings() { return servings; }
    public void setServings(Integer servings) { this.servings = servings; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

