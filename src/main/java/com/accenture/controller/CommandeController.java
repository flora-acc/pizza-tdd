package com.accenture.controller;

import com.accenture.service.Interface.CommandeService;
import com.accenture.service.dto.CommandeRequestDto;
import com.accenture.service.dto.CommandeResponseDto;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.shared.Statut;
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
@RequestMapping("/commandes")
@Slf4j
@Tag(name = "Gestion des Commandes", description = "Interface de gestion des Commandes")
public class CommandeController {

    private CommandeService commandeService;

    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }



    @Operation(summary = "Creer une Commande")
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
    @Operation(summary = "Trouver une Commande")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Commande trouvée"),
    @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle")})
    @GetMapping("/{id}")
    ResponseEntity<CommandeResponseDto> trouverParId( @PathVariable int id){
        CommandeResponseDto commandeResponseDto = commandeService.trouverParId(id);
        log.info("Commande Par id : {}", commandeResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(commandeResponseDto);
    }
    @Operation(summary = "Trouver les Commandes")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Commandes trouvées"),
    @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle")})
    @GetMapping
    ResponseEntity<List<CommandeResponseDto>> trouverToutes(){
        List<CommandeResponseDto> liste = commandeService.trouverToutes();
        log.info("Commandes : {}", liste);
        return ResponseEntity.status(HttpStatus.OK).body(liste);
    }
    @Operation(summary = "Modifier Statut Commande")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statut modifié"),
            @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle")})
    @PatchMapping("/{id}")
    ResponseEntity<CommandeResponseDto> modifierCommande(@PathVariable int id, @RequestBody Statut statut){
        CommandeResponseDto commandeResponseDto = commandeService.modifierStatus(id, statut);
        log.info("Commande modification Status : {}", commandeResponseDto);
        return ResponseEntity.ok(commandeResponseDto);
    }

    @Operation(summary = "Trouver par Statut Commande")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trouvées"),
            @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle")})
    @GetMapping("/statut")
    ResponseEntity<List<CommandeResponseDto>> trouverParStatus(@RequestParam Statut statut){
        List<CommandeResponseDto> commandeResponseDto = commandeService.trouverParStatut(statut);
        log.info("Commandes: {}", commandeResponseDto);
        return ResponseEntity.ok(commandeResponseDto);
    }

}
