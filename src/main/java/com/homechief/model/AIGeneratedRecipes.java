package com.homechief.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AIGeneratedRecipes")
public class AIGeneratedRecipes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "RecipeID")
    private Recipes recipe;

    @Column(name = "PromptData", columnDefinition = "json")
    private String promptData;

    @Column(name = "GeneratedContent", columnDefinition = "json")
    private String generatedContent;

    @Column(name = "Status")
    private String status = "pending";

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    // Getters and setters (no Lombok)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Recipes getRecipe() { return recipe; }
    public void setRecipe(Recipes recipe) { this.recipe = recipe; }

    public String getPromptData() { return promptData; }
    public void setPromptData(String promptData) { this.promptData = promptData; }

    public String getGeneratedContent() { return generatedContent; }
    public void setGeneratedContent(String generatedContent) { this.generatedContent = generatedContent; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
