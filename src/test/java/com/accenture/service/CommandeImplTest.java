package com.accenture.service;

import com.accenture.exception.CommandeException;
import com.accenture.repository.CommandeDao;
import com.accenture.service.impl.CommandeServiceImpl;
import com.accenture.service.dto.CommandeRequestDto;
import com.accenture.service.dto.PizzaTailleQteRequestDto;
import com.accenture.shared.Taille;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CommandeImplTest {

    @InjectMocks
    private CommandeServiceImpl commandeServiceImpl;

    @Mock
    private CommandeDao commandeDao;

    @Test
    void testAjoutCommandeNull() {
        CommandeException ex = Assertions.assertThrows(CommandeException.class, () -> commandeServiceImpl.ajouter(null));
        Assertions.assertEquals("La pizzaTailleQteList est null", ex.getMessage());
    }

    @Test
    void testAjoutCommandeClientNull() {
        CommandeRequestDto commande = new CommandeRequestDto(null,null);
        CommandeException ex = Assertions.assertThrows(CommandeException.class, () -> commandeServiceImpl.ajouter(commande));
        Assertions.assertEquals("Le client est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutCommandeCommandeRequestNull() {
        CommandeRequestDto commande = new CommandeRequestDto(1,null);
        CommandeException ex = Assertions.assertThrows(CommandeException.class, () -> commandeServiceImpl.ajouter(commande));
        Assertions.assertEquals("Le contenu de la pizzaTailleQteList est obligatoire", ex.getMessage());
    }
    @Test
    void testAjoutCommandeCommandeRequestIdPizzaNull() {
        CommandeRequestDto commande = new CommandeRequestDto(1,List.of(new PizzaTailleQteRequestDto(null, Taille.GRANDE, 10)));
        CommandeException ex = Assertions.assertThrows(CommandeException.class, () -> commandeServiceImpl.ajouter(commande));
        Assertions.assertEquals("L'Id de la Pizza est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutCommandeCommandeRequestTailleNull() {
        CommandeRequestDto commande = new CommandeRequestDto(1,List.of(new PizzaTailleQteRequestDto(1, null, 10)));
        CommandeException ex = Assertions.assertThrows(CommandeException.class, () -> commandeServiceImpl.ajouter(commande));
        Assertions.assertEquals("La taille est obligatoire", ex.getMessage());
    }
 @Test
    void testAjoutCommandeCommandeRequestQuantiteNull() {
        CommandeRequestDto commande = new CommandeRequestDto(1,List.of(new PizzaTailleQteRequestDto(1, Taille.GRANDE, null)));
        CommandeException ex = Assertions.assertThrows(CommandeException.class, () -> commandeServiceImpl.ajouter(commande));
        Assertions.assertEquals("La quantite est obligatoire", ex.getMessage());
    }


}
