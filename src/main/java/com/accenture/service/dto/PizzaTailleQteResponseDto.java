package com.accenture.service.dto;

import com.accenture.shared.Taille;

public record PizzaTailleQteResponseDto (
        String pizzaNom,
        Taille taille,
        Integer quantite

) {
}
