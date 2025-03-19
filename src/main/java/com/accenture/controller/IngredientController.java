package com.accenture.controller;

import com.accenture.service.Interface.IngredientService;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import com.accenture.shared.Filtre;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


import java.util.List;

@RestController
@RequestMapping("/ingredients")
@Slf4j
@Tag(name = "Gestion des Ingrédients", description = "Interface de gestion des Ingrédients")
public class IngredientController {

    private IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }


    @Operation(summary = "Ajouter un ingrédient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ingrédient ajouté"),
            @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle")
    })
    @PostMapping
    ResponseEntity<IngredientResponseDto> ajouterIngredient(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Ingrédient Création", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IngredientRequestDto.class),
                    examples = @ExampleObject(value = """
                            {
                            
                            "nom": "Tomate",
                              "quantite": 3
                            }
                            """
                    ))) @RequestBody IngredientRequestDto ingredientRequest) {
        IngredientResponseDto ingredientResponse = ingredientService.ajouter(ingredientRequest);
        log.info("ajout : {} ", ingredientResponse);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(("/{id}"))
                .buildAndExpand(ingredientResponse.id())
                .toUri();
        return ResponseEntity.created(location).body(ingredientResponse);
    }

    @Operation(summary = "Afficher un ingrédient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingrédient trouvé"),
            @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle"),
    })
    @GetMapping("/{id}")
    ResponseEntity<IngredientResponseDto> trouverParId(@PathVariable int id) {
        IngredientResponseDto ingredientResponse = ingredientService.trouverParId(id);
        log.info("trouverParId : {}", ingredientResponse);
        return ResponseEntity.ok(ingredientResponse);
    }

    @Operation(summary = "Afficher les ingrédients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingrédients trouvés"),
            @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle"),
    })
    @GetMapping
    List<IngredientResponseDto> afficherTousLesIngredients() {
        log.info("Requête reçue pour récupérer la liste des ingrédients.");
        List<IngredientResponseDto> ingredients = ingredientService.afficherTousIngredients();
        log.debug("Nombre d'ingrédients trouvés' : {}", ingredients.size());
        return ingredients;
    }

    @Operation(summary = "Modifier partiellement un ingrédient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingrédient modifié"),
            @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle"),
    })
    @PatchMapping("/{id}")
    ResponseEntity<IngredientResponseDto> modifierIngredientPartiellement(@PathVariable int id, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Ingrédient Création", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IngredientRequestDto.class),
                    examples = @ExampleObject(value = """
                            {
                            
                            "nom": "Tomate",
                              "quantite": 3
                            }
                            """
                    )))@RequestBody IngredientRequestDto ingredientRequest){
        log.info("Requête reçue pour récupérer un ingrédient.");
        IngredientResponseDto reponse = ingredientService.modifierPartiellementIngredient(id, ingredientRequest);
        return ResponseEntity.ok(reponse);

    }



}
