package com.accenture.service.Interface;

import com.accenture.exception.IngredientException;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface IngredientService {
    IngredientResponseDto ajouter(IngredientRequestDto ingredientRequest);
    IngredientResponseDto trouverParId(int id);

    List<IngredientResponseDto> afficherTousIngredients() throws IngredientException;

    IngredientResponseDto modifierPartiellementIngredient(int id, IngredientRequestDto ingredientRequest) throws EntityNotFoundException;
}
