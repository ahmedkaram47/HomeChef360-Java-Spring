package com.homechief.repository;

import com.homechief.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Integer> {
    @Query("SELECT s FROM ShoppingList s WHERE s.user.id = :userId")
    List<ShoppingList> findByUserId(Integer userId);
}
