package com.accenture.service.Interface;

import com.accenture.exception.IngredientException;
import com.accenture.service.dto.IngredientRequest;
import com.accenture.service.dto.IngredientResponse;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.action.internal.EntityActionVetoException;

import java.util.List;

public interface IngredientService {
    IngredientResponse ajouter(IngredientRequest ingredientRequest);
    IngredientResponse trouverParId(int id);

    List<IngredientResponse> afficherTousIngredients() throws IngredientException;

    IngredientResponse modifierPartiellementIngredient(int id, IngredientRequest ingredientRequest) throws EntityNotFoundException;
}
