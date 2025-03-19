package com.accenture.controller;

import com.accenture.service.Interface.PizzaService;
import com.accenture.service.dto.IngredientRequestDto;

import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import com.accenture.shared.Filtre;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
                             },
                             "commandable" : true
                             }
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
    @DeleteMapping
    ResponseEntity<PizzaResponseDto> supprimerPizzaDeLaCarte(@RequestParam int id){
       PizzaResponseDto pizzaResponseDto = pizzaService.supprimerDeLaCarteParId(id);
        log.info("Supression de la carte : {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(pizzaResponseDto);
    }

    @GetMapping()
    ResponseEntity<List> trouverToutesParFiltre (@RequestParam (required = false) Filtre filtre){
        List<PizzaResponseDto> liste = pizzaService.trouverToutes(filtre);
        log.info("Trouver Pizzas : {}", liste);
       return ResponseEntity.status(HttpStatus.OK).body(liste);
    }

    @GetMapping("/{id}")
    ResponseEntity<PizzaResponseDto> trouverParId (@PathVariable int id){
        PizzaResponseDto pizzaResponseDto = pizzaService.trouverParId(id);
       log.info("trouver par Id : {}", pizzaResponseDto);
       return ResponseEntity.ok(pizzaResponseDto);
    }
}
