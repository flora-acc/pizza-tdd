package com.accenture.service.dto;

import com.accenture.shared.Status;
import com.accenture.shared.Taille;

import java.time.LocalDate;
import java.util.List;

public record CommandeResponseDto(
      int id,
      int idClient,
      List<PizzaTailleQteResponseDto> pizzaTailleQteResponseDtoList,
      LocalDate date,
      Status status,
      Double prix
) {
}
