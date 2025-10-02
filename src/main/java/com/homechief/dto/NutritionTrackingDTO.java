package com.homechief.dto;

import java.time.LocalDate;

public class NutritionTrackingDTO {
    private Integer id;
    private LocalDate date;
    private Integer calories;
    private Integer protein;
    private Integer carbs;
    private Integer fat;
    private Integer fiber;
    private String notes;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }

    public Integer getProtein() { return protein; }
    public void setProtein(Integer protein) { this.protein = protein; }

    public Integer getCarbs() { return carbs; }
    public void setCarbs(Integer carbs) { this.carbs = carbs; }

    public Integer getFat() { return fat; }
    public void setFat(Integer fat) { this.fat = fat; }

    public Integer getFiber() { return fiber; }
    public void setFiber(Integer fiber) { this.fiber = fiber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
