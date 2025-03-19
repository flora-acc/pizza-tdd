package com.accenture.service.dto;

import com.accenture.shared.Taille;


import java.util.List;
import java.util.Map;

public record PizzaRequestDto(
        String nom,
        List<Integer> idIngredient,
        Map<Taille, Double> prix
) {
}
