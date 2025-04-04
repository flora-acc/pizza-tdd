package com.accenture.service.impl;

import com.accenture.exception.PizzaException;
import com.accenture.repository.IngredientDao;
import com.accenture.repository.PizzaDao;
import com.accenture.repository.model.Ingredient;
import com.accenture.repository.model.Pizza;
import com.accenture.service.inter.PizzaService;
import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import com.accenture.shared.Filtre;
import jakarta.persistence.EntityNotFoundException;
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
    public PizzaResponseDto ajouter(PizzaRequestDto pizzaRequest) {
        verifierPizza(pizzaRequest);
        return toPizzaResponse(pizzaDao.save(toPizza(pizzaRequest)));

    }

    @Override
    public PizzaResponseDto supprimerDeLaCarteParId(int id) {
        Optional<Pizza> optPizza = pizzaDao.findById(id);
        if (optPizza.isEmpty()) {
            EntityNotFoundException entityNotFoundException = new EntityNotFoundException("Aucune Pizza à cette id");
            log.error("Erreur Suppression pizza: {} ", entityNotFoundException.getMessage());
            throw entityNotFoundException;
        }
        Pizza pizza = optPizza.get();
        pizza.setCommandable(false);
        return toPizzaResponse(pizzaDao.save(pizza));
    }

    @Override
    public List<PizzaResponseDto> trouverToutes(Filtre filtre) {
        return switch (filtre) {
            case COMMANDABLE ->
                    pizzaDao.findByCommandableTrue().stream().map(this::toPizzaResponse).toList();
            case RETIREE -> pizzaDao.findByCommandableFalse().stream().map(this::toPizzaResponse).toList();
            case null -> pizzaDao.findAll().stream().map(this::toPizzaResponse).toList();
        };
    }

    @Override
    public PizzaResponseDto trouverParId(int id) throws EntityNotFoundException {
        Optional<Pizza> optionalPizza = pizzaDao.findById(id);
        if (optionalPizza.isEmpty()) {
            EntityNotFoundException ex = new EntityNotFoundException("Aucune Pizza à cet ID");
            log.error("Erreur trouverParId : {} ", ex.getMessage());
            throw ex;
        }
        return toPizzaResponse(optionalPizza.get());
    }

    @Override
    public List<PizzaResponseDto> trouverParNom(String nom) {
        if (nom == null || nom.isBlank())
            throw new PizzaException("La recherche ne doit pas être blank");

        return pizzaDao.findByNomContaining(nom).stream().filter(Pizza::getCommandable).map(this::toPizzaResponse).toList();
    }

    @Override
    public List<PizzaResponseDto> trouverPizzaParIdIngredient(int ingredientId) throws EntityNotFoundException {
        Optional<Ingredient> optionalIngredient = ingredientDao.findById(ingredientId);

        if (optionalIngredient.isEmpty()) {
            EntityNotFoundException ex = new EntityNotFoundException("Aucun ingrédient trouvé avec cet ID");
            log.error("Erreur trouverPizzaParIngredientId : {}", ex.getMessage());
            throw ex;
        }

        List<Pizza> pizzas = pizzaDao.findByIngredientsId(ingredientId);

        return pizzas
                .stream()
                .map(this::toPizzaResponse)
                .toList();
    }


    //*************************************************************************
//    ************************ METHODES PRIVEES *******************************
//    *************************************************************************


    private static void verifierPizza(PizzaRequestDto pizzaRequest) {
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
        if (pizzaRequest.prix().containsValue(null)) {
            message = "Un prix ne doit pas être null";
            log.error(ERREUR_VERIFICATION_PIZZA, message);
            throw new PizzaException(message);
        }
        if (pizzaRequest.prix().containsValue((double) 0)) {
            message = "Un prix ne doit pas être égal à 0";
            log.error(ERREUR_VERIFICATION_PIZZA, message);
            throw new PizzaException(message);
        }

        if (pizzaRequest.commandable() == null) {
            message = "Précisez si la pizza est commandable";
            log.error(ERREUR_VERIFICATION_PIZZA, message);
            throw new PizzaException(message);
        }
    }


    private PizzaResponseDto toPizzaResponse(Pizza pizza1) {
        List<String> listeIngredient = new ArrayList<>();
        pizza1.getIngredients().forEach(ingredient -> listeIngredient.add(ingredient.getNom()));
        return new PizzaResponseDto(pizza1.getId(), pizza1.getNom(), listeIngredient, pizza1.getPrix(), pizza1.getCommandable());
    }

    private Pizza toPizza(PizzaRequestDto pizzaRequest) {

        Pizza pizza = new Pizza();
        pizza.setNom(pizzaRequest.nom());
        pizza.setIngredients(ingredientDao.findAllById(pizzaRequest.idIngredient()));
        pizza.setPrix(pizzaRequest.prix());
        pizza.setCommandable(pizzaRequest.commandable());
        return pizza;
    }
}
