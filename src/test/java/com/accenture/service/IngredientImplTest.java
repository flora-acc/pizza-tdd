package com.accenture.service;

import com.accenture.exception.IngredientException;
import com.accenture.repository.IngredientDao;
import com.accenture.repository.model.Ingredient;
import com.accenture.service.Impl.IngredientServiceImpl;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import com.accenture.service.mapper.IngredientMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IngredientImplTest {

    @InjectMocks
    private IngredientServiceImpl ingredientServiceImpl;

    @Mock
    IngredientMapper ingredientMapperMock;

    @Mock
    IngredientDao ingredientDao;

    @Test
    void testAjoutIngredientNull() {
        IngredientException ex = Assertions.assertThrows(IngredientException.class, () -> ingredientServiceImpl.ajouter(null));
        Assertions.assertEquals("L'ingrédient est null", ex.getMessage());
    }


    @Test
    void testAjoutIngredientQuantiteNull() {
        IngredientRequestDto tomate = new IngredientRequestDto("Tomate", null);
        IngredientException ex = Assertions.assertThrows(IngredientException.class, () -> ingredientServiceImpl.ajouter(tomate));
        Assertions.assertEquals("La quantité est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutIngredientQuantiteNegative() {
        IngredientRequestDto tomate = new IngredientRequestDto("Tomate", -4);
        IngredientException ex = Assertions.assertThrows(IngredientException.class, () -> ingredientServiceImpl.ajouter(tomate));
        Assertions.assertEquals("La quantité doit être supérieure ou égale à 0", ex.getMessage());
    }

    @Test
    void testAjoutIngredientNomNull() {
        IngredientRequestDto tomate = new IngredientRequestDto(null, 3);
        IngredientException ex = Assertions.assertThrows(IngredientException.class, () -> ingredientServiceImpl.ajouter(tomate));
        Assertions.assertEquals("Le nom est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutIngredientNomBlank() {
        IngredientRequestDto tomate = new IngredientRequestDto(" ", 3);
        IngredientException ex = Assertions.assertThrows(IngredientException.class, () -> ingredientServiceImpl.ajouter(tomate));
        Assertions.assertEquals("Le nom est obligatoire", ex.getMessage());
    }

    @Test
    void testAjouterOk() {
        IngredientRequestDto tomateRequete = new IngredientRequestDto("Tomate", 3);
        IngredientResponseDto tomateReponse = new IngredientResponseDto(1, "Tomate", 3);

        Ingredient tomateAvant = new Ingredient("Tomate", 3);
        Ingredient tomateApres = new Ingredient(1, "Tomate", 3);

        Mockito.when(ingredientMapperMock.toIngredient(tomateRequete)).thenReturn(tomateAvant);
        Mockito.when(ingredientDao.save(tomateAvant)).thenReturn(tomateApres);
        Mockito.when(ingredientMapperMock.toIngredientResponse(tomateApres)).thenReturn(tomateReponse);

        Assertions.assertEquals(tomateReponse, ingredientServiceImpl.ajouter(tomateRequete));

        Mockito.verify(ingredientDao).save(tomateAvant);

    }

    @Test
    void testTrouverParIdAvecIdIntrouvable() {
        Mockito.when(ingredientDao.findById(1)).thenReturn(Optional.empty());
        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class, () -> ingredientServiceImpl.trouverParId(1));
        Assertions.assertEquals("L'id n'existe pas en base", ex.getMessage());
    }

    @Test
    void testTrouverParIdOk() {
        Ingredient tomate = new Ingredient(1, "Tomate", 3);
        IngredientResponseDto tomateReponse = new IngredientResponseDto(1, "Tomate", 3);

        Mockito.when(ingredientDao.findById(1)).thenReturn(Optional.of(tomate));
        Mockito.when(ingredientMapperMock.toIngredientResponse(tomate)).thenReturn(tomateReponse);

        Assertions.assertEquals(tomateReponse, ingredientServiceImpl.trouverParId(1));
    }

    @DisplayName("""
            Test de la méthode afficherTousIngredients qui doit renvoyer une liste d'ingredients
            correspondant aux ingredients existant en base
            """)
    @Test
    void testAfficherTousIngredients() {
        Ingredient tomate = new Ingredient("Tomate", 3);
        Ingredient parmesan = new Ingredient("Parmesan", 7);
        List<Ingredient> ingredients = List.of(tomate, parmesan);

        IngredientResponseDto tomateReponse = new IngredientResponseDto(1, "Tomate", 3);
        IngredientResponseDto parmesanReponse = new IngredientResponseDto(2, "Parmesan", 7);
        List<IngredientResponseDto> dtos = List.of(tomateReponse, parmesanReponse);

        Mockito.when(ingredientDao.findAll()).thenReturn(ingredients);
        Mockito.when(ingredientMapperMock.toIngredientResponse(tomate)).thenReturn((tomateReponse));
        Mockito.when(ingredientMapperMock.toIngredientResponse(parmesan)).thenReturn((parmesanReponse));

        assertEquals(dtos, ingredientServiceImpl.afficherTousIngredients());
    }

    @DisplayName(" Test modifierPartiellementIngredient() : " +
            "si l'id est inexistant dans la base de données, l'exception est levée")
    @Test
    void testModifierPartiellementIngredientIdIntrouvable() {
        IngredientRequestDto ingredientRequest = new IngredientRequestDto("Tomate", 3);
        Mockito.when(ingredientDao.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class,
                () -> ingredientServiceImpl.modifierPartiellementIngredient(1, ingredientRequest));
        Assertions.assertEquals("Cet ingrédient est introuvable", ex.getMessage());
    }

    @DisplayName("""
            Test de la méthode modifierPartiellementIngredientNom qui 
            enregistre la modification en BDD
            """)
    @Test
    void testModifierPartiellementIngredientNom() {
        //renvoie un objet de type Ingredient
        Ingredient ingredientExistant = new Ingredient("Tomate", 3);
        //créer le DTO de mise à jour
        IngredientRequestDto ingredientRequest = new IngredientRequestDto("NouveauNom", ingredientExistant.getQuantite());
        IngredientRequestDto ingredientRequest2 = new IngredientRequestDto("NouveauNom", ingredientExistant.getQuantite());
        // on crée un nouvel ingrédient - copie de ingredientExistant pour comparer
        Ingredient ingredientMisAJour = new Ingredient("Tomate", 3);
        ingredientMisAJour.setNom("NouveauNom");

        // réponse attendue après modification
        IngredientResponseDto ingredientResponse = new IngredientResponseDto(1, "NouveauNom", ingredientExistant.getQuantite());
        //On dit à Mockito de renvoyer un Optional contenant ingredientExistant
        Mockito.when(ingredientDao.findById(1)).thenReturn((Optional.of(ingredientExistant)));
        // simule la sauvegarde du client dans la base de données
        Mockito.when(ingredientDao.save(Mockito.any(Ingredient.class))).thenReturn(ingredientMisAJour);
        // convertit le Client mis à jour en un ClientResponseDto
        Mockito.when(ingredientMapperMock.toIngredientRequest(ingredientMisAJour)).thenReturn(ingredientRequest2);

        Mockito.when(ingredientMapperMock.toIngredientResponse(ingredientMisAJour)).thenReturn(ingredientResponse);


        // appel de la méthode à tester
        IngredientResponseDto response = ingredientServiceImpl.modifierPartiellementIngredient(1, ingredientRequest);

        // Si la méthode fonctionne correctement, elle renvoit un DTO
        assertNotNull(response);
        assertEquals("NouveauNom", response.nom());
        Mockito.verify(ingredientDao, Mockito.times(1)).save(ingredientExistant);
    }

    @DisplayName("""
            Test de la méthode modifierPartiellementIngredientQuantite qui 
            enregistre la modification en BDD
            """)
    @Test
    void testModifierPartiellementIngredientQuantite() {
        //renvoie un objet de type Ingredient
        Ingredient ingredientExistant = new Ingredient("Tomate", 3);
        //créer le DTO de mise à jour
        IngredientRequestDto ingredientRequest = new IngredientRequestDto("Tomate", -4);
        IngredientRequestDto ingredientRequest2 = new IngredientRequestDto("Tomate", -4);
        // on crée un nouvel ingrédient - copie de ingredientExistant pour comparer
        Ingredient ingredientMisAJour = new Ingredient("Tomate", -4);

        // réponse attendue après modification
        IngredientResponseDto ingredientResponse = new IngredientResponseDto(1, "Tomate", -4);
        //On dit à Mockito de renvoyer un Optional contenant ingredientExistant
        Mockito.when(ingredientDao.findById(1)).thenReturn((Optional.of(ingredientExistant)));

        // Convertit le mise à jour en Request pour la fonction vérification
        Mockito.when(ingredientMapperMock.toIngredientRequest(ingredientMisAJour)).thenReturn(ingredientRequest2);
        IngredientException ex = Assertions.assertThrows(IngredientException.class, () -> ingredientServiceImpl.modifierPartiellementIngredient(1, ingredientRequest));
        Assertions.assertEquals("La quantité doit être supérieure ou égale à 0", ex.getMessage());
    }

   @DisplayName("""
            Test de la méthode modifierPartiellement en passant Null à la request
            """)
    @Test
    void testModifierPartiellementIngredientRequestNull() {
        //renvoie un objet de type Ingredient
        Ingredient ingredientExistant = new Ingredient("Tomate", 3);
        //créer le DTO de mise à jour
        IngredientRequestDto ingredientRequest = new IngredientRequestDto(null, null);
        IngredientRequestDto ingredientRequest2 = new IngredientRequestDto("Tomate", 3);
        // on crée un nouvel ingrédient - copie de ingredientExistant pour comparer
        Ingredient ingredientMisAJour = new Ingredient("Tomate", 3);

        // réponse attendue après modification
        IngredientResponseDto ingredientResponse = new IngredientResponseDto(1, "Tomate", -4);
        //On dit à Mockito de renvoyer un Optional contenant ingredientExistant
       Mockito.when(ingredientDao.findById(1)).thenReturn((Optional.of(ingredientExistant)));
       // simule la sauvegarde du client dans la base de données
       Mockito.when(ingredientDao.save(Mockito.any(Ingredient.class))).thenReturn(ingredientMisAJour);
       // convertit le Client mis à jour en un ClientResponseDto
       Mockito.when(ingredientMapperMock.toIngredientRequest(ingredientMisAJour)).thenReturn(ingredientRequest2);

       Mockito.when(ingredientMapperMock.toIngredientResponse(ingredientMisAJour)).thenReturn(ingredientResponse);


       // appel de la méthode à tester
       IngredientResponseDto response = ingredientServiceImpl.modifierPartiellementIngredient(1, ingredientRequest);

       // Si la méthode fonctionne correctement, elle renvoit un DTO
       assertNotNull(response);
       assertEquals("Tomate", response.nom());
       Mockito.verify(ingredientDao, Mockito.times(1)).save(ingredientExistant);
    }


}
