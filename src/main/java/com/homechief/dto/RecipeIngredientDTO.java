package com.homechief.dto;

public class RecipeIngredientDTO {
    private Integer ingredientId;  // maps to RecipeIngredients.ID
    private String ingredientName;
    private String quantity;  // now String
    private String Unit ;     // now String

    // --- Getters & Setters ---
    public Integer getIngredientId() {
        return ingredientId;
    }
    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }



    public String getUnit() {
        return Unit;
    }
    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

}
