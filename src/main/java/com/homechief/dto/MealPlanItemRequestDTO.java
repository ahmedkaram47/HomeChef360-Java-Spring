package com.homechief.dto;

public class MealPlanItemRequestDTO {
    private String dayOfWeek;
    private String mealType;
    private Integer recipeId; // nullable
    private String customMealName;
    private String customMealDescription;
    private Integer calories;
    private Integer prepTime;

    public MealPlanItemRequestDTO() {}

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public Integer getRecipeId() { return recipeId; }
    public void setRecipeId(Integer recipeId) { this.recipeId = recipeId; }

    public String getCustomMealName() { return customMealName; }
    public void setCustomMealName(String customMealName) { this.customMealName = customMealName; }

    public String getCustomMealDescription() { return customMealDescription; }
    public void setCustomMealDescription(String customMealDescription) { this.customMealDescription = customMealDescription; }

    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }

    public Integer getPrepTime() { return prepTime; }
    public void setPrepTime(Integer prepTime) { this.prepTime = prepTime; }
}
