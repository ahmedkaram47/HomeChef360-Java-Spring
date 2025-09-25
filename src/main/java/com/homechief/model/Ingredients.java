package com.homechief.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Ingredients")
public class Ingredients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Name", nullable = false, unique = true)
    private String name;

    @Column(name = "Category")
    private String category;

    @Column(name = "Unit")
    private String unit;

    @Column(name = "CaloriesPerUnit")
    private Double caloriesPerUnit;

    @Column(name = "ProteinPerUnit")
    private Double proteinPerUnit;

    @Column(name = "CarbsPerUnit")
    private Double carbsPerUnit;

    @Column(name = "FatPerUnit")
    private Double fatPerUnit;

    @Column(name = "FiberPerUnit")
    private Double fiberPerUnit;

    @Column(name = "SugarPerUnit")
    private Double sugarPerUnit;

    @Column(name = "SodiumPerUnit")
    private Double sodiumPerUnit;

    @Column(name = "ImageUrl")
    private String imageUrl;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "CreatedAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Ingredients() {}

    // Getters and setters...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Double getCaloriesPerUnit() { return caloriesPerUnit; }
    public void setCaloriesPerUnit(Double caloriesPerUnit) { this.caloriesPerUnit = caloriesPerUnit; }

    public Double getProteinPerUnit() { return proteinPerUnit; }
    public void setProteinPerUnit(Double proteinPerUnit) { this.proteinPerUnit = proteinPerUnit; }

    public Double getCarbsPerUnit() { return carbsPerUnit; }
    public void setCarbsPerUnit(Double carbsPerUnit) { this.carbsPerUnit = carbsPerUnit; }

    public Double getFatPerUnit() { return fatPerUnit; }
    public void setFatPerUnit(Double fatPerUnit) { this.fatPerUnit = fatPerUnit; }

    public Double getFiberPerUnit() { return fiberPerUnit; }
    public void setFiberPerUnit(Double fiberPerUnit) { this.fiberPerUnit = fiberPerUnit; }

    public Double getSugarPerUnit() { return sugarPerUnit; }
    public void setSugarPerUnit(Double sugarPerUnit) { this.sugarPerUnit = sugarPerUnit; }

    public Double getSodiumPerUnit() { return sodiumPerUnit; }
    public void setSodiumPerUnit(Double sodiumPerUnit) { this.sodiumPerUnit = sodiumPerUnit; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setCreatedAt(LocalDateTime now)
    {
        this.createdAt = now;
    }

    public void setUpdatedAt(LocalDateTime now)
    {
        this.updatedAt = now;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
