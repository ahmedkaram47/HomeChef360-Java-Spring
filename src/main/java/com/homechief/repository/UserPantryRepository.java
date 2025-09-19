package com.homechief.repository;

import com.homechief.model.UserPantry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPantryRepository extends JpaRepository<UserPantry, Integer> {
    List<UserPantry> findByUserId(Integer userId);
}
