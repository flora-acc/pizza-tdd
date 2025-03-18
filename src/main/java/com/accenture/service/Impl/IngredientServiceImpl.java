package com.accenture.service.Impl;

import com.accenture.exception.IngredientException;
import com.accenture.repository.IngredientDao;
import com.accenture.repository.model.Ingredient;
import com.accenture.service.Interface.IngredientService;
import com.accenture.service.dto.IngredientRequest;
import com.accenture.service.dto.IngredientResponse;
import com.accenture.service.mapper.IngredientMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    public static final String ERREUR_VERIFICATION_INGREDIENT = "Erreur verification Ingrédient : {}";
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


    @Override
    public List<IngredientResponse> afficherTousIngredients() throws IngredientException {
        return ingredientDao.findAll().stream()
                .map(ingredient -> ingredientMapper.toIngredientResponse(ingredient))
                .toList();
    }

    @Override
    public IngredientResponse modifierPartiellementIngredient(int id, IngredientRequest ingredientRequest) throws EntityNotFoundException {

        Ingredient ingredientExistant = ingredientDao.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Cet ingrédient est introuvable"));

        remplacer(ingredientRequest, ingredientExistant);
        verificationIngredient(ingredientMapper.toIngredientRequest(ingredientExistant));
        Ingredient ingredientMisAJour = ingredientDao.save(ingredientExistant);
        return ingredientMapper.toIngredientResponse(ingredientMisAJour);
    }

//    *************************************************************************
//    ************************ METHODES PRIVEES *******************************
//    *************************************************************************

    private static void verificationIngredient(IngredientRequest ingredientRequest) {
        String message = "";
        if (ingredientRequest == null) {
            message = "L'ingrédient est null";
            log.error(ERREUR_VERIFICATION_INGREDIENT, message);
            throw new IngredientException(message);
        }
        if (ingredientRequest.quantite() == null) {
            message = "La quantité est obligatoire";
            log.error(ERREUR_VERIFICATION_INGREDIENT, message);
            throw new IngredientException(message);
        }

        if (ingredientRequest.quantite() < 0) {
            message = "La quantité doit être supérieure ou égale à 0";
            log.error(ERREUR_VERIFICATION_INGREDIENT, message);
            throw new IngredientException(message);
        }

        if (ingredientRequest.nom() == null || ingredientRequest.nom().isBlank()){
            message = "Le nom est obligatoire";
            log.error(ERREUR_VERIFICATION_INGREDIENT, message);
            throw new IngredientException(message);
        }

    }

    private static void remplacer(IngredientRequest ingredientRequest, Ingredient ingredientExistant) {
        if (ingredientRequest.nom() != null)
            ingredientExistant.setNom(ingredientRequest.nom());
        if (ingredientRequest.quantite() != null)
            ingredientExistant.setQuantite(ingredientRequest.quantite());
    }


}
