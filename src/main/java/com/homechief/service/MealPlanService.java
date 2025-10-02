package com.homechief.service;

import com.homechief.dto.*;
import com.homechief.model.*;
import com.homechief.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealPlanService {

    private final MealPlansRepository mealPlansRepository;
    private final MealPlanItemsRepository mealPlanItemsRepository;
    private final UserRepository userRepository;
    private final RecipesRepository recipesRepository;

    public MealPlanService(MealPlansRepository mealPlansRepository,
                           MealPlanItemsRepository mealPlanItemsRepository,
                           UserRepository userRepository,
                           RecipesRepository recipesRepository) {
        this.mealPlansRepository = mealPlansRepository;
        this.mealPlanItemsRepository = mealPlanItemsRepository;
        this.userRepository = userRepository;
        this.recipesRepository = recipesRepository;
    }

    public List<MealPlanDTO> getMealPlansForUser(Integer userId) {
        return mealPlansRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MealPlanDTO createMealPlan(Integer userId, MealPlanRequestDTO req) {
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        MealPlans m = new MealPlans();
        m.setUser(user);
        m.setName(req.getName());
        m.setDescription(req.getDescription());
        m.setStartDate(req.getStartDate());
        m.setEndDate(req.getEndDate());
        m.setTotalCalories(req.getTotalCalories());
        m.setDifficulty(req.getDifficulty());
        m.setCuisine(req.getCuisine());
        MealPlans saved = mealPlansRepository.save(m);
        return toDTO(saved);
    }

    public MealPlanDTO getMealPlan(Integer id) {
        return mealPlansRepository.findById(id).map(this::toDTO).orElse(null);
    }

    @Transactional
    public MealPlanDTO updateMealPlan(Integer id, Integer userId, MealPlanRequestDTO req) {
        MealPlans m = mealPlansRepository.findById(id).orElseThrow(() -> new RuntimeException("MealPlan not found"));
        if (m.getUser() == null || !m.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to update this meal plan");
        }
        m.setName(req.getName());
        m.setDescription(req.getDescription());
        m.setStartDate(req.getStartDate());
        m.setEndDate(req.getEndDate());
        m.setTotalCalories(req.getTotalCalories());
        m.setDifficulty(req.getDifficulty());
        m.setCuisine(req.getCuisine());
        return toDTO(mealPlansRepository.save(m));
    }

    public void deleteMealPlan(Integer id, Integer userId) {
        MealPlans m = mealPlansRepository.findById(id).orElseThrow(() -> new RuntimeException("MealPlan not found"));
        if (m.getUser() == null || !m.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this meal plan");
        }
        mealPlansRepository.delete(m);
    }

    // ---------------- Items ----------------

    public List<MealPlanItemDTO> getItemsForMealPlan(Integer mealPlanId) {
        return mealPlanItemsRepository.findByMealPlanId(mealPlanId).stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
    }

    public MealPlanItemDTO addItem(Integer mealPlanId, Integer userId, MealPlanItemRequestDTO req) {
        MealPlans m = mealPlansRepository.findById(mealPlanId).orElseThrow(() -> new RuntimeException("MealPlan not found"));
        if (m.getUser() == null || !m.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to add items to this meal plan");
        }
        MealPlanItems item = new MealPlanItems();
        item.setMealPlan(m);
        item.setDayOfWeek(req.getDayOfWeek());
        item.setMealType(req.getMealType());
        if (req.getRecipeId() != null) {
            Recipes r = recipesRepository.findById(req.getRecipeId()).orElse(null);
            item.setRecipe(r);
        } else {
            item.setRecipe(null);
        }
        item.setCustomMealName(req.getCustomMealName());
        item.setCustomMealDescription(req.getCustomMealDescription());
        item.setCalories(req.getCalories());
        item.setPrepTime(req.getPrepTime());

        MealPlanItems saved = mealPlanItemsRepository.save(item);
        return toItemDTO(saved);
    }

    public MealPlanItemDTO updateItem(Integer mealPlanId, Integer itemId, Integer userId, MealPlanItemRequestDTO req) {
        MealPlanItems item = mealPlanItemsRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        if (item.getMealPlan() == null || !item.getMealPlan().getId().equals(mealPlanId)) {
            throw new RuntimeException("Item does not belong to the specified meal plan");
        }
        if (item.getMealPlan().getUser() == null || !item.getMealPlan().getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to update this item");
        }
        item.setDayOfWeek(req.getDayOfWeek());
        item.setMealType(req.getMealType());
        if (req.getRecipeId() != null) {
            Recipes r = recipesRepository.findById(req.getRecipeId()).orElse(null);
            item.setRecipe(r);
        } else {
            item.setRecipe(null);
        }
        item.setCustomMealName(req.getCustomMealName());
        item.setCustomMealDescription(req.getCustomMealDescription());
        item.setCalories(req.getCalories());
        item.setPrepTime(req.getPrepTime());

        MealPlanItems saved = mealPlanItemsRepository.save(item);
        return toItemDTO(saved);
    }

    public void deleteItem(Integer mealPlanId, Integer itemId, Integer userId) {
        MealPlanItems item = mealPlanItemsRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        if (item.getMealPlan() == null || !item.getMealPlan().getId().equals(mealPlanId)) {
            throw new RuntimeException("Item does not belong to the specified meal plan");
        }
        if (item.getMealPlan().getUser() == null || !item.getMealPlan().getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized to delete this item");
        }
        mealPlanItemsRepository.delete(item);
    }

    // ----------------- mapping helpers -----------------

    private MealPlanDTO toDTO(MealPlans m) {
        MealPlanDTO dto = new MealPlanDTO();
        dto.setId(m.getId());
        dto.setName(m.getName());
        dto.setDescription(m.getDescription());
        dto.setStartDate(m.getStartDate());
        dto.setEndDate(m.getEndDate());
        dto.setTotalCalories(m.getTotalCalories());
        dto.setDifficulty(m.getDifficulty());
        dto.setCuisine(m.getCuisine());
        dto.setCreatedAt(m.getCreatedAt());
        dto.setUpdatedAt(m.getUpdatedAt());
        return dto;
    }

    private MealPlanItemDTO toItemDTO(MealPlanItems item) {
        MealPlanItemDTO dto = new MealPlanItemDTO();
        dto.setId(item.getId());
        dto.setDayOfWeek(item.getDayOfWeek());
        dto.setMealType(item.getMealType());
        if (item.getRecipe() != null) {
            dto.setRecipeId(item.getRecipe().getId());
            dto.setRecipeName(item.getRecipe().getName());
        }
        dto.setCustomMealName(item.getCustomMealName());
        dto.setCustomMealDescription(item.getCustomMealDescription());
        dto.setCalories(item.getCalories());
        dto.setPrepTime(item.getPrepTime());
        dto.setCreatedAt(item.getCreatedAt());
        return dto;
    }
}
