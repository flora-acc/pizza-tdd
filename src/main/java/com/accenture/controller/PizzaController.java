package com.accenture.controller;

import com.accenture.service.Interface.PizzaService;
import com.accenture.service.dto.IngredientRequestDto;

import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Slf4j
@RequestMapping("/pizzas")
@Tag(name = "Gestion des Pizzas", description = "Interface de gestion des Pizzas")
public class PizzaController {

    private PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }
    @Operation(summary = "Ajouter une Pizza")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pizza ajoutée"),
            @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle")
    })

    @PostMapping
    ResponseEntity<PizzaResponseDto> ajouterPizza(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Pizza Création", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IngredientRequestDto.class),
                    examples = @ExampleObject(value = """
                             {"nom": "Margarita",
                             "idIngredient": [
                               1, 2
                             ],
                             "prix": {
                               "PETITE": 10.0,
                               "MOYENNE": 12.0,
                               "GRANDE": 14.0
                             }}
                            """
                    )))@RequestBody PizzaRequestDto pizzaRequest){
        PizzaResponseDto pizzaResponseDto = pizzaService.ajouter(pizzaRequest);
        log.info("ajout : {} ", pizzaResponseDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(("/{id}"))
                .buildAndExpand(pizzaResponseDto.id())
                .toUri();
        return ResponseEntity.created(location).body(pizzaResponseDto);
    }
}
