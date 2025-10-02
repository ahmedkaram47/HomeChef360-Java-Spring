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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column(name = "Prompt", columnDefinition = "TEXT")
    private String prompt;

    @Column(name = "GeneratedRecipe", columnDefinition = "TEXT")
    private String generatedRecipe;

    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public String getGeneratedRecipe() { return generatedRecipe; }
    public void setGeneratedRecipe(String generatedRecipe) { this.generatedRecipe = generatedRecipe; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
