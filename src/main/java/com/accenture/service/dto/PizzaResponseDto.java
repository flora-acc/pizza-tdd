package com.accenture.service.dto;

import com.accenture.shared.Taille;

import java.util.EnumMap;
import java.util.List;

public record PizzaResponseDto (
        int id,
        String nom,
        List<String> ingredients,
        EnumMap<Taille, Double> prix
) {
}
