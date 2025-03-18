package com.accenture.service.Interface;

import com.accenture.repository.model.Pizza;
import com.accenture.service.dto.PizzaRequest;
import com.accenture.service.dto.PizzaResponseDto;

public interface PizzaService {

    PizzaResponseDto ajouter(PizzaRequest pizzaRequest);
}
