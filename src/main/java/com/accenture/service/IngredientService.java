package com.accenture.service;

import com.accenture.exception.IngredientException;
import com.accenture.service.dto.IngredientRequest;
import com.accenture.service.dto.IngredientResponse;

import java.util.List;

public interface IngredientService {
    IngredientResponse ajouter(IngredientRequest ingredientRequest);
    IngredientResponse trouverParId(int id);

    List<IngredientResponse> afficherTousIngredients() throws IngredientException;
}
