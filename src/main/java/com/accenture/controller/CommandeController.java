package com.accenture.controller;

import com.accenture.service.Interface.CommandeService;
import com.accenture.service.dto.CommandeRequestDto;
import com.accenture.service.dto.CommandeResponseDto;
import com.accenture.service.dto.IngredientRequestDto;
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

@RestController
@RequestMapping("/commandes")
@Slf4j
@Tag(name = "Gestion des Commandes", description = "Interface de gestion des Commandes")
public class CommandeController {

    private CommandeService commandeService;

    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }



    @Operation(summary = "Trouver une commande")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Commande ajoutée"),
            @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle")
    })
    @PostMapping
    ResponseEntity<CommandeResponseDto> ajouter(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Pizza Création", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IngredientRequestDto.class),
                    examples = @ExampleObject(value = """
                            {
                              "idClient": 1,
                              "pizzaTailleQteList": [
                                {
                                  "idPizza": 1,
                                  "taille": "PETITE",
                                  "quantite": 5
                                },
                             {
                                  "idPizza": 2,
                                  "taille": "PETITE",
                                  "quantite": 1
                                }
                              ]
                            }
                            """
                    )))@RequestBody CommandeRequestDto commandeRequestDto){
        CommandeResponseDto commandeResponseDto = commandeService.ajouter(commandeRequestDto);
        log.info("Ajout Commande : {}", commandeResponseDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(("/{id}"))
                .buildAndExpand(commandeResponseDto.id())
                .toUri();
       return ResponseEntity.created(location).body(commandeResponseDto);
    }
}
