package com.homechief.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MealPlanItems")
public class MealPlanItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "MealPlanID", nullable = false)
    private MealPlans mealPlan;

    @Column(name = "DayOfWeek")
    private String dayOfWeek;

    @Column(name = "MealType")
    private String mealType;

    @ManyToOne
    @JoinColumn(name = "RecipeID")
    private Recipes recipe;

    @Column(name = "CustomMealName")
    private String customMealName;

    @Column(name = "CustomMealDescription")
    private String customMealDescription;

    @Column(name = "Calories")
    private Integer calories;

    @Column(name = "PrepTime")
    private Integer prepTime;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public MealPlans getMealPlan() { return mealPlan; }
    public void setMealPlan(MealPlans mealPlan) { this.mealPlan = mealPlan; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public Recipes getRecipe() { return recipe; }
    public void setRecipe(Recipes recipe) { this.recipe = recipe; }

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
