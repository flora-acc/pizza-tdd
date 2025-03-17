package com.accenture.service;

import com.accenture.exception.IngredientException;
import com.accenture.repository.IngredientDao;
import com.accenture.repository.model.Ingredient;
import com.accenture.service.dto.IngredientRequest;
import com.accenture.service.dto.IngredientResponse;
import com.accenture.service.mapper.IngredientMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private IngredientDao ingredientDao;
    private IngredientMapper ingredientMapper;

    @Override
    public IngredientResponse ajouter(IngredientRequest ingredientRequest) {
        verificationIngredient(ingredientRequest);
        return ingredientMapper.toIngredientResponse(
                ingredientDao.save(
                        ingredientMapper.toIngredient(ingredientRequest)));
    }

    @Override
    public IngredientResponse trouverParId(int id) {
        Optional<Ingredient> optIngredient = ingredientDao.findById(id);
        if (optIngredient.isEmpty())
            throw new EntityNotFoundException("L'id n'existe pas en base");
        return ingredientMapper.toIngredientResponse(optIngredient.get());
    }


//    *************************************************************************
//    ************************ METHODES PRIVEES *******************************
//    *************************************************************************

    private static void verificationIngredient(IngredientRequest ingredientRequest) {
        if (ingredientRequest == null)
            throw new IngredientException("L'ingrédient est null");
        if (ingredientRequest.quantite() == null)
            throw new IngredientException("La quantité est obligatoire");
        if (ingredientRequest.quantite() < 0)
            throw new IngredientException("La quantité doit être supérieure ou égale à 0");
        if (ingredientRequest.nom() == null || ingredientRequest.nom().isBlank())
            throw new IngredientException("Le nom est obligatoire");
    }
}
