package com.accenture.repository;

import com.accenture.repository.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientDao extends JpaRepository<Ingredient, Integer> {

    Optional<Ingredient> findById(int id);
}
