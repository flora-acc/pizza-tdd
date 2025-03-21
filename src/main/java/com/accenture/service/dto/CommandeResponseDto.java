package com.accenture.service.dto;

import com.accenture.shared.Statut;

import java.time.LocalDate;
import java.util.List;

public record CommandeResponseDto(
      int id,
      int idClient,
      List<PizzaTailleQteResponseDto> pizzaTailleQteResponseDtoList,
      LocalDate date,
      Statut statut,
      Double prix
) {
}
