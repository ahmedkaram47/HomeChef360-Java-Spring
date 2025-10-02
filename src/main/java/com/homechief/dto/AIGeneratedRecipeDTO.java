package com.homechief.dto;

import java.time.LocalDateTime;

public class AIGeneratedRecipeDTO {
    private Integer id;
    private String prompt;
    private String generatedRecipe;
    private LocalDateTime createdAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public String getGeneratedRecipe() { return generatedRecipe; }
    public void setGeneratedRecipe(String generatedRecipe) { this.generatedRecipe = generatedRecipe; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
