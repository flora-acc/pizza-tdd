package com.accenture.repository;

import com.accenture.repository.model.Pizza;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PizzaDao extends JpaRepository<Pizza, Integer> {

    List<Pizza> findByCommandableTrue();
    List<Pizza> findByCommandableFalse();

    List<Pizza> findByNomContaining(String nom);
    List<Pizza> findByIngredientsId(int ingredientId);
}
