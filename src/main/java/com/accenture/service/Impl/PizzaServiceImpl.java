package com.accenture.service.Impl;

import com.accenture.exception.IngredientException;
import com.accenture.exception.PizzaException;
import com.accenture.repository.IngredientDao;
import com.accenture.repository.PizzaDao;
import com.accenture.repository.model.Ingredient;
import com.accenture.repository.model.Pizza;
import com.accenture.service.Interface.PizzaService;
import com.accenture.service.dto.PizzaRequest;
import com.accenture.service.dto.PizzaResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PizzaServiceImpl implements PizzaService {

    public static final String ERREUR_VERIFICATION_PIZZA = "Erreur vérification Pizza : {}";
    private IngredientDao ingredientDao;
    private PizzaDao pizzaDao;


    @Override
    public PizzaResponseDto ajouter(PizzaRequest pizzaRequest) {
        verifierPizza(pizzaRequest);
        return toPizzaResponse(pizzaDao.save(toPizza(pizzaRequest)));

    }




    //*************************************************************************
//    ************************ METHODES PRIVEES *******************************
//    *************************************************************************


    private static void verifierPizza(PizzaRequest pizzaRequest) {
        String message;

        if (pizzaRequest == null) {
            message = "La pizza est null";
            log.error(ERREUR_VERIFICATION_PIZZA, message);
            throw new PizzaException(message);
        }
        if (pizzaRequest.nom() == null || pizzaRequest.nom().isBlank()) {
            message = "Le nom de la pizza est obligatoire";
            log.error(ERREUR_VERIFICATION_PIZZA, message);
            throw new PizzaException(message);
        }
        if (pizzaRequest.idIngredient() == null) {
            message = "La liste d'ingrédients est obligatoire";
            log.error(ERREUR_VERIFICATION_PIZZA, message);
            throw new PizzaException(message);
        }
        if (pizzaRequest.idIngredient().size() < 2) {
            message = "La pizza doit utiliser deux ingrédients ou plus";
            log.error(ERREUR_VERIFICATION_PIZZA, message);
            throw new PizzaException(message);
        }
        if ((pizzaRequest.prix() == null)) {
            message = "La liste des prix est obligatoire";
            log.error(ERREUR_VERIFICATION_PIZZA, message);
            throw new PizzaException(message);
        }
        if (pizzaRequest.prix().size() != 3) {
            message = "Tous les prix par taille sont obligatoires";
            log.error(ERREUR_VERIFICATION_PIZZA, message);
            throw new PizzaException(message);
        }
        if (pizzaRequest.prix().containsValue(null)){
            message = "Un prix ne doit pas être null";
            log.error(ERREUR_VERIFICATION_PIZZA, message);
            throw new PizzaException(message);}
        if (pizzaRequest.prix().containsValue((double) 0)){
            message = "Un prix ne doit pas être égal à 0";
            log.error(ERREUR_VERIFICATION_PIZZA, message);
            throw new PizzaException(message);}
        }

    private static PizzaResponseDto toPizzaResponse(Pizza pizza1) {
        List<String> listeIngredient = new ArrayList<>();
        pizza1.getIngredients().forEach(ingredient -> listeIngredient.add(ingredient.getNom()));
        return new PizzaResponseDto(pizza1.getId(), pizza1.getNom(), listeIngredient, pizza1.getPrix());
    }

    private Pizza toPizza(PizzaRequest pizzaRequest) {

        List<Ingredient> ingredients = new ArrayList<>();
        Optional<Ingredient> optionalIngredient;

        for (int i = 1; i <= pizzaRequest.idIngredient().size(); i++) {
            optionalIngredient = ingredientDao.findById(i);
            if (optionalIngredient.isEmpty()) {
                IngredientException ingredientException = new IngredientException("Ingrédient Invalide");
                log.error("Erreur ingrédients : {} ", ingredientException.getMessage());
                throw ingredientException;
            }
            ingredients.add(optionalIngredient.get());
        }

        Pizza pizza = new Pizza();
        pizza.setNom(pizzaRequest.nom());
        pizza.setIngredients(ingredients);
        pizza.setPrix(pizzaRequest.prix());
        return pizza;
    }
}
