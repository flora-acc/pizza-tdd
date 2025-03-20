package com.accenture.service.dto;

import com.accenture.shared.Taille;

public record PizzaTailleQteRequestDto(Integer idPizza, Taille taille, Integer quantite) {
}
