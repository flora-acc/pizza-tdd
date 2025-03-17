package com.accenture.controller;

import com.accenture.service.IngredientService;
import com.accenture.service.dto.IngredientRequest;
import com.accenture.service.dto.IngredientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
@Slf4j
public class IngredientController {

    private IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    ResponseEntity<IngredientResponse> ajouterIngredient(@RequestBody IngredientRequest ingredientRequest) {
        IngredientResponse ingredientResponse = ingredientService.ajouter(ingredientRequest);
        log.info("ajout : {} ", ingredientResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientResponse);
    }

    @GetMapping
    List<IngredientResponse> afficherTousIngredients() {
        log.info("Requête reçue pour récupérer la liste des ingrédients.");
        List<IngredientResponse> ingredients = ingredientService.afficherTousIngredients();
        log.debug("Nombre d'ingrédients trouvés' : {}", ingredients.size());
        return ingredients;
    }

}
