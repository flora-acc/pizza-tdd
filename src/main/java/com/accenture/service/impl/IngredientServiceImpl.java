package com.accenture.service.impl;

import com.accenture.exception.IngredientException;
import com.accenture.repository.IngredientDao;
import com.accenture.repository.model.Ingredient;
import com.accenture.service.inter.IngredientService;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
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

    /**
     * Ajoute un ingrédient dans la base données
     * @param ingredientRequest Objet contenant les informations de l'ingrédient à ajouter
     * @return un objet IngredientResponseDto qui représente l'ingrédient à ajouter
     */
    @Override
    public IngredientResponseDto ajouter(IngredientRequestDto ingredientRequest) {
        verificationIngredient(ingredientRequest);
        return ingredientMapper.toIngredientResponse(
                ingredientDao.save(
                        ingredientMapper.toIngredient(ingredientRequest)));
    }

    /**
     * Trouve un ingredient dans la base de données grâces à son identifiant
     * @param id Identifiant de l'ingredient à trouver
     * @return Un objet IngredientResponseDto représentant l'ingredient trouvé
     * @throws EntityNotFoundException si aucun ingredient n'est trouvé
     */
    @Override
    public IngredientResponseDto trouverParId(int id) throws EntityNotFoundException {
        Optional<Ingredient> optIngredient = ingredientDao.findById(id);
        if (optIngredient.isEmpty())
            throw new EntityNotFoundException("L'id n'existe pas en base");
        return ingredientMapper.toIngredientResponse(optIngredient.get());
    }

    /**
     * Récupère la liste de tous les ingrédients dans la base de données
     * @return une liste d'objets IngredientResponseDto représentant tous les ingrédients
     */
    @Override
    public List<IngredientResponseDto> afficherTousIngredients() throws IngredientException {
        return ingredientDao.findAll().stream()
                .map(ingredient -> ingredientMapper.toIngredientResponse(ingredient))
                .toList();
    }

    /**
     * Met à jour partiellement un ingrédient existant dans la base de données
     * @param id Identifiant de l'ingrédient à mettre à jour
     * @param ingredientRequest Objet contenant les informations à mettre à jour
     * @return Un objet IngredientResponseDto représentant l'ingrédient mise à jour
     * @throws EntityNotFoundException Si aucun ingrédient n'est trouvé pour cet id
     */
    @Override
    public IngredientResponseDto modifierPartiellementIngredient(int id, IngredientRequestDto ingredientRequest) throws EntityNotFoundException {

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

    private static void verificationIngredient(IngredientRequestDto ingredientRequest) {
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

    private static void remplacer(IngredientRequestDto ingredientRequest, Ingredient ingredientExistant) {
        if (ingredientRequest.nom() != null)
            ingredientExistant.setNom(ingredientRequest.nom());
        if (ingredientRequest.quantite() != null)
            ingredientExistant.setQuantite(ingredientRequest.quantite());
    }


}
