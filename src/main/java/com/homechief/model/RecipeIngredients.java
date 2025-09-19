package com.homechief.model;

import jakarta.persistence.*;

@Entity
@Table(name = "RecipeIngredients")
public class RecipeIngredients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    // Many ingredients belong to one recipe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RecipeID", nullable = false)
    private Recipes recipe;

    @Column(name = "IngredientName", nullable = false)
    private String ingredientName;

    @Column(name = "Quantity")
    private String quantity;

    public RecipeIngredients() {}

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Recipes getRecipe() { return recipe; }
    public void setRecipe(Recipes recipe) { this.recipe = recipe; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
}
