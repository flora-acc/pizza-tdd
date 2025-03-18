package com.accenture.service.dto;

import com.accenture.shared.Taille;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

public record PizzaRequest (
        String nom,
        List<Integer> idIngredient,
        EnumMap<Taille, Double> prix
) {
}
