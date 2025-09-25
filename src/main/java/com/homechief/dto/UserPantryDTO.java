package com.homechief.dto;

import java.time.LocalDate;

public class UserPantryDTO {
    private Integer id;
    private IngredientDTO ingredient;
    private double quantity;
    private String unit;
    private String category;
    private LocalDate expiryDate;
    private String notes;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public IngredientDTO getIngredient() { return ingredient; }
    public void setIngredient(IngredientDTO ingredient) { this.ingredient = ingredient; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
