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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
                            "prenom": "Michel"
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
}
