package com.homechief.dto;

import java.time.LocalDateTime;

public class MealPlanItemDTO {
    private Integer id;
    private String dayOfWeek;
    private String mealType;
    private Integer recipeId;
    private String recipeName;
    private String customMealName;
    private String customMealDescription;
    private Integer calories;
    private Integer prepTime;
    private LocalDateTime createdAt;

    public MealPlanItemDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public Integer getRecipeId() { return recipeId; }
    public void setRecipeId(Integer recipeId) { this.recipeId = recipeId; }

    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }

    public String getCustomMealName() { return customMealName; }
    public void setCustomMealName(String customMealName) { this.customMealName = customMealName; }

    public String getCustomMealDescription() { return customMealDescription; }
    public void setCustomMealDescription(String customMealDescription) { this.customMealDescription = customMealDescription; }

    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }

    public Integer getPrepTime() { return prepTime; }
    public void setPrepTime(Integer prepTime) { this.prepTime = prepTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
