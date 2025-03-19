package com.accenture.service;

import com.accenture.exception.ClientException;
import com.accenture.exception.IngredientException;
import com.accenture.repository.ClientDao;
import com.accenture.service.Impl.ClientServiceImpl;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.mapper.ClientMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void testAjoutClientNomNull() {
        ClientRequestDto client1 = new ClientRequestDto("test", null,"test@email.fr");
        ClientException ex = Assertions.assertThrows(ClientException.class, () -> clientServiceImpl.ajouterClient(client1));
        Assertions.assertEquals("Le nom est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutClientNomBlank() {
        ClientRequestDto client1 = new ClientRequestDto("test", "\t","test@email.fr");
        ClientException ex = Assertions.assertThrows(ClientException.class, () -> clientServiceImpl.ajouterClient(client1));
        Assertions.assertEquals("Le nom est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutClientPreNomNull() {
        ClientRequestDto client1 = new ClientRequestDto(null, "test","test@email.fr");
        ClientException ex = Assertions.assertThrows(ClientException.class, () -> clientServiceImpl.ajouterClient(client1));
        Assertions.assertEquals("Le prenom est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutClientPreNomBlank() {
        ClientRequestDto client1 = new ClientRequestDto("\t", "test","test@email.fr");
        ClientException ex = Assertions.assertThrows(ClientException.class, () -> clientServiceImpl.ajouterClient(client1));
        Assertions.assertEquals("Le prenom est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutClientEmailNull() {
        ClientRequestDto client1 = new ClientRequestDto("test", "test",null);
        ClientException ex = Assertions.assertThrows(ClientException.class, () -> clientServiceImpl.ajouterClient(client1));
        Assertions.assertEquals("L'email est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutClientEmailBlank() {
        ClientRequestDto client1 = new ClientRequestDto("test", "test","\t");
        ClientException ex = Assertions.assertThrows(ClientException.class, () -> clientServiceImpl.ajouterClient(client1));
        Assertions.assertEquals("L'email est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutClientEmailInvalide() {
        ClientRequestDto client1 = new ClientRequestDto("test", "test","formatInvalide");
        ClientException ex = Assertions.assertThrows(ClientException.class, () -> clientServiceImpl.ajouterClient(client1));
        Assertions.assertEquals("Format de l'email invalide", ex.getMessage());
    }


    @ParameterizedTest
    @CsvSource(value={  "test,test,\t,L'email est obligatoire",
                        "test,test,null,L'email est obligatoire",
                        "test,test,formatInvalide,Format de l'email invalide",
                        "null,test,test@email.fr,Le prenom est obligatoire",
                        "\t,test,test@email.fr,Le prenom est obligatoire",
                        "test,null,test@email.fr,Le nom est obligatoire",
                        "test,\t,test@email.fr,Le nom est obligatoire"
                        })
    void testAjoutClientInvalide(String prenom, String nom, String email, String message) {
        prenom = transformNull(prenom);
        nom = transformNull(nom);
        email = transformNull(email);
        ClientRequestDto client1 = new ClientRequestDto(prenom,nom,email);
        ClientException ex = Assertions.assertThrows(ClientException.class, () -> clientServiceImpl.ajouterClient(client1));
        Assertions.assertEquals(message, ex.getMessage());
    }

    private static String transformNull(String chaine) {
        return "null".equals(chaine) ? null : chaine;
    }
}
