package com.homechief.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ShoppingList")
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RecipeID", referencedColumnName = "ID", nullable = true)
    private Recipes recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IngredientID", referencedColumnName = "ID", nullable = true)
    private Ingredients ingredient;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Quantity")
    private String quantity;

    @Column(name = "Unit")
    private String unit;

    @Column(name = "Status")
    private String status = "Pending";

    @Column(name = "CreatedAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public ShoppingList() {}

    // Getters & setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Recipes getRecipe() { return recipe; }
    public void setRecipe(Recipes recipe) { this.recipe = recipe; }

    public Ingredients getIngredient() { return ingredient; }
    public void setIngredient(Ingredients ingredient) { this.ingredient = ingredient; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
