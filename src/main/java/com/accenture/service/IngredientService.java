package com.accenture.service;

import com.accenture.service.dto.IngredientRequest;
import com.accenture.service.dto.IngredientResponse;

public interface IngredientService {
    IngredientResponse ajouter(IngredientRequest ingredientRequest);
}
