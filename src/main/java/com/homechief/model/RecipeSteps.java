package com.homechief.model;

import com.homechief.model.Recipes;
import jakarta.persistence.*;

@Entity
@Table(name = "RecipeSteps")
public class RecipeSteps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RecipeID", nullable = false)
    private Recipes recipe;

    @Column(name = "StepNumber", nullable = false)
    private Integer stepNumber;

    @Column(name = "Instruction", columnDefinition = "TEXT", nullable = false)
    private String instruction;

    public RecipeSteps() {}

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Recipes getRecipe() { return recipe; }
    public void setRecipe(Recipes recipe) { this.recipe = recipe; }

    public Integer getStepNumber() { return stepNumber; }
    public void setStepNumber(Integer stepNumber) { this.stepNumber = stepNumber; }

    public String getInstruction() { return instruction; }
    public void setInstruction(String instruction) { this.instruction = instruction; }
}
