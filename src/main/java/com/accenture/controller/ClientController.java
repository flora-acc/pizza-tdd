package com.accenture.controller;

import com.accenture.service.Interface.ClientService;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
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
@RequestMapping("/clients")
@Slf4j
@Tag(name = "Gestion des clients", description = "Interface de gestion des clients")
public class ClientController {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Ajouter un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "client ajouté"),
            @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle")
    })
    @PostMapping
    ResponseEntity<ClientResponseDto> ajouterUnClient(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Création d'un client", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClientRequestDto.class),
                    examples = @ExampleObject(value = """
                            {
                            "prenom": "Michel",
                            "nom": "Dupont",
                             "email": "email@email.fr"
                            }
                            """
                    ))) @RequestBody ClientRequestDto clientRequest) {
        ClientResponseDto clientResponse = clientService.ajouterClient(clientRequest);
        log.info("ajout : {} ", clientResponse);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(("/{id}"))
                .buildAndExpand(clientResponse.id())
                .toUri();
        return ResponseEntity.created(location).body(clientResponse);
    }

    @Operation(summary = "Afficher un client grâce à son id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client trouvé"),
            @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle"),
    })
    @GetMapping("/{id}")
    ResponseEntity<ClientResponseDto> trouverParId(@PathVariable int id) {
        ClientResponseDto clientResponse = clientService.trouverParId(id);
        log.info("trouverParId : {}", clientResponse);
        return ResponseEntity.ok(clientResponse);
    }

    @Operation(summary = "Afficher les clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients trouvés"),
            @ApiResponse(responseCode = "400", description = "Erreur Fonctionnelle"),
    })
    @GetMapping
    List<ClientResponseDto> afficherTousLesClients() {
        log.info("Requête reçue pour récupérer la liste des clients.");
        List<ClientResponseDto> clients = clientService.afficherTousClients();
        log.debug("Nombre de clients trouvés' : {}", clients.size());
        return clients;
    }
}
