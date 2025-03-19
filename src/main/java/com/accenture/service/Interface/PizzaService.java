package com.accenture.service.Interface;

import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;

public interface PizzaService {

    PizzaResponseDto ajouter(PizzaRequestDto pizzaRequest);

    PizzaResponseDto supprimerDeLaCarteParId(int id);
}
