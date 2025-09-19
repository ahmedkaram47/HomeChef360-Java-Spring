package com.homechief.repository;

import com.homechief.model.ShoppingList;
import com.homechief.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Integer> {
    List<ShoppingList> findByUser(User user);
}
