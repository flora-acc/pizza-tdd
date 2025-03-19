package com.accenture.service.Interface;

import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import com.accenture.shared.Filtre;

import java.util.List;

public interface PizzaService {

    PizzaResponseDto ajouter(PizzaRequestDto pizzaRequest);

    PizzaResponseDto supprimerDeLaCarteParId(int id);

    List<PizzaResponseDto> trouverToutes(Filtre filtre);
}
