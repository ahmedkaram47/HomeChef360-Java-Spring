package com.homechief.service;

import com.homechief.dto.*;
import com.homechief.model.*;
import com.homechief.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingListService {
    private final ShoppingListRepository listRepo;
    private final UserRepository userRepo;
    private final IngredientsRepository ingredientsRepo;
    private final RecipesRepository recipesRepo;

    public ShoppingListService(ShoppingListRepository listRepo,
                               UserRepository userRepo,
                               IngredientsRepository ingredientsRepo,
                               RecipesRepository recipesRepo) {
        this.listRepo = listRepo;
        this.userRepo = userRepo;
        this.ingredientsRepo = ingredientsRepo;
        this.recipesRepo = recipesRepo;
    }

    public List<ShoppingListDTO> getUserLists(Integer userId) {
        return listRepo.findByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ShoppingListDTO createList(Integer userId, ShoppingListRequestDTO req) {
        User u = userRepo.findById(Long.valueOf(userId)).orElseThrow();
        ShoppingList list = new ShoppingList();
        list.setUser(u);
        list.setName(req.getName());
        list.setQuantity(req.getQuantity());
        list.setStatus(req.getStatus());
        list.setUnit(req.getUnit());
        if (req.getIngredientId() != null)
            list.setIngredient(ingredientsRepo.findById(req.getIngredientId()).orElseThrow());
        if (req.getRecipeId() != null)
            list.setRecipe(recipesRepo.findById(req.getRecipeId()).orElseThrow());
        return toDTO(listRepo.save(list));
    }

    public ShoppingListDTO updateList(Integer listId, Integer userId, ShoppingListRequestDTO req) {
        ShoppingList list = listRepo.findById(listId).orElseThrow();
        if (!list.getUser().getId().equals(userId)) throw new RuntimeException("Unauthorized");
        list.setName(req.getName());
        list.setQuantity(req.getQuantity());
        list.setStatus(req.getStatus());
        list.setUnit(req.getUnit());
        if (req.getIngredientId() != null)
            list.setIngredient(ingredientsRepo.findById(req.getIngredientId()).orElseThrow());
        if (req.getRecipeId() != null)
            list.setRecipe(recipesRepo.findById(req.getRecipeId()).orElseThrow());
        return toDTO(listRepo.save(list));
    }

    public void deleteList(Integer listId, Integer userId) {
        ShoppingList list = listRepo.findById(listId).orElseThrow();
        if (!list.getUser().getId().equals(userId)) throw new RuntimeException("Unauthorized");
        listRepo.delete(list);
    }

    private ShoppingListDTO toDTO(ShoppingList list) {
        ShoppingListDTO dto = new ShoppingListDTO();
        dto.setId(list.getId());
        dto.setName(list.getName());
        dto.setQuantity(list.getQuantity());
        dto.setStatus(list.getStatus());
        dto.setUnit(list.getUnit());
        dto.setIngredientId(list.getIngredient() != null ? list.getIngredient().getId() : null);
        dto.setRecipeId(list.getRecipe() != null ? list.getRecipe().getId() : null);
        dto.setCreatedAt(list.getCreatedAt());
        return dto;
    }
}
