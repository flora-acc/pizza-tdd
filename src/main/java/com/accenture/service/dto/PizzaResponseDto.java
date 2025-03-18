package com.accenture.service.dto;

import com.accenture.shared.Taille;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record PizzaResponseDto (
        int id,
        String nom,
        List<String> ingredients,
        Map<Taille, Double> prix
) {
}
