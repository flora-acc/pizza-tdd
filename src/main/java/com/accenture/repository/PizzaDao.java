package com.accenture.repository;

import com.accenture.repository.model.Pizza;
import com.accenture.service.dto.PizzaResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PizzaDao extends JpaRepository<Pizza, Integer> {

    List<Pizza> findByCommandableTrue();
    List<Pizza> findByCommandableFalse();

    List<Pizza> findByNomContaining(String nom);
    List<Pizza> findByIngredientsId(int ingredientId);
}
