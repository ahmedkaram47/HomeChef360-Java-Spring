package com.homechief.dto;

public class RecipeStepRequestDTO {
    private Integer stepNumber;
    private String instruction;

    public RecipeStepRequestDTO() {}

    public Integer getStepNumber() { return stepNumber; }
    public void setStepNumber(Integer stepNumber) { this.stepNumber = stepNumber; }

    public String getInstruction() { return instruction; }
    public void setInstruction(String instruction) { this.instruction = instruction; }
}
