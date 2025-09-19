package com.homechief.model;

import com.homechief.model.Recipes;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserFavorites")
public class UserFavorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RecipeID", nullable = false)
    private Recipes recipe;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    public UserFavorites() {}

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Recipes getRecipe() { return recipe; }
    public void setRecipe(Recipes recipe) { this.recipe = recipe; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
