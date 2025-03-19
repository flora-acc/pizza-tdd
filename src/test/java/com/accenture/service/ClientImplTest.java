package com.accenture.service;

import com.accenture.exception.ClientException;
import com.accenture.exception.IngredientException;
import com.accenture.repository.ClientDao;
import com.accenture.repository.model.Client;
import com.accenture.repository.model.Commande;
import com.accenture.repository.model.Ingredient;
import com.accenture.service.Impl.ClientServiceImpl;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import com.accenture.service.mapper.ClientMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ClientImplTest {

    @InjectMocks
    ClientServiceImpl clientServiceImpl;

    @Mock
    ClientMapper clientMapper;

    @Mock
    ClientDao clientDao;

    @Test
    void testAjoutClientNull() {
        ClientException ex = Assertions.assertThrows(ClientException.class, () -> clientServiceImpl.ajouterClient(null));
        Assertions.assertEquals("Le client est introuvable", ex.getMessage());
    }


    @ParameterizedTest
    @CsvSource(value = {"test,test, ,L'email est obligatoire",
            "test,test,null,L'email est obligatoire",
            "test,test,formatInvalide,Format de l'email invalide",
            "null,test,test@email.fr,Le prenom est obligatoire",
            " ,test,test@email.fr,Le prenom est obligatoire",
            "test,null,test@email.fr,Le nom est obligatoire",
            "test, ,test@email.fr,Le nom est obligatoire"
    })
    void testAjoutClientInvalide(String prenom, String nom, String email, String message) {
        prenom = transformNull(prenom);
        nom = transformNull(nom);
        email = transformNull(email);
        ClientRequestDto client1 = new ClientRequestDto(prenom, nom, email);
        ClientException ex = Assertions.assertThrows(ClientException.class, () -> clientServiceImpl.ajouterClient(client1));
        Assertions.assertEquals(message, ex.getMessage());
    }

    private static String transformNull(String chaine) {
        return "null".equals(chaine) ? null : chaine;
    }

    @Test
    void testAjouterOk() {
        ClientRequestDto clientRequestDto = new ClientRequestDto("Test", "Test", "test@email.fr");
        Commande commande1 = new Commande(1);
        ClientResponseDto clientResponseDto = new ClientResponseDto(1, "Test", "Test", "test@email.fr", List.of(commande1));

        Client clientAvant = new Client("Test1", "Test1", "test@email.fr", List.of(commande1));
        Client clientApres = new Client(1, "Test2", "Test2", "test@email.fr", List.of(commande1));

        Mockito.when(clientMapper.toClient(clientRequestDto)).thenReturn(clientAvant);
        Mockito.when(clientDao.save(clientAvant)).thenReturn(clientApres);
        Mockito.when(clientMapper.toClientResponseDto(clientApres)).thenReturn(clientResponseDto);

        Assertions.assertEquals(clientResponseDto, clientServiceImpl.ajouterClient(clientRequestDto));

        Mockito.verify(clientDao).save(clientAvant);
    }

    @Test
    void testTrouverParIdAvecIdIntrouvable() {
        Mockito.when(clientDao.findById(1)).thenReturn(Optional.empty());
        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class, () -> clientServiceImpl.trouverParId(1));
        Assertions.assertEquals("L'id n'existe pas en base", ex.getMessage());
    }

    @Test
    void testTrouverParIdOk() {
        Commande commande1 = new Commande(1);
        Client client1 = new Client("Test1","Test1","test@email.fr",List.of(commande1));
        ClientResponseDto clientResponseDto = new ClientResponseDto(1, "Test", "Test","test@email.fr", List.of(commande1));

        Mockito.when(clientDao.findById(1)).thenReturn(Optional.of(client1));
        Mockito.when(clientMapper.toClientResponseDto(client1)).thenReturn(clientResponseDto);

        Assertions.assertEquals(clientResponseDto, clientServiceImpl.trouverParId(1));
    }

    @DisplayName("""
            Test de la méthode afficherTousClients qui doit renvoyer une liste de clients
            correspondant aux clients existant en base
            """)
    @Test
    void testAfficherTousClients() {
        Commande commande1 = new Commande(1);
        Client client1 = new Client("Test1", "Test1", "test@email.fr", List.of(commande1));
        Client client2 = new Client(1, "Test2", "Test2", "test@email.fr", List.of(commande1));
        List<Client> clients = List.of(client1, client2);

        ClientResponseDto client1Response = new ClientResponseDto(1,"Test1", "Test1", "test@email.fr", List.of(commande1));
        ClientResponseDto client2Response = new ClientResponseDto(2, "Test2", "Test2", "test@email.fr", List.of(commande1));
        List<ClientResponseDto> dtos = List.of(client1Response, client2Response);

        Mockito.when(clientDao.findAll()).thenReturn(clients);
        Mockito.when(clientMapper.toClientResponseDto(client1)).thenReturn((client1Response));
        Mockito.when(clientMapper.toClientResponseDto(client2)).thenReturn((client2Response));

        assertEquals(dtos, clientServiceImpl.afficherTousClients());
    }
}
