package com.accenture.service.dto;

import com.accenture.shared.Taille;

import java.util.List;

public record CommandeResponseDto(
      int id,
      int idClient,
      List<PizzaTailleQteResponseDto> pizzaTailleQteResponseDtoList

) {
}
