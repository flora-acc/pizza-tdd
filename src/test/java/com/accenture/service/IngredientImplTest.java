package com.accenture.service;

import com.accenture.exception.IngredientException;
import com.accenture.repository.IngredientDao;
import com.accenture.repository.model.Ingredient;
import com.accenture.service.dto.IngredientRequest;
import com.accenture.service.dto.IngredientResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        IngredientRequest tomate = new IngredientRequest("Tomate", null);
        IngredientException ex = Assertions.assertThrows(IngredientException.class, () -> ingredientServiceImpl.ajouter(tomate));
        Assertions.assertEquals("La quantité est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutIngredientQuantiteNegative() {
        IngredientRequest tomate = new IngredientRequest("Tomate", -4);
        IngredientException ex = Assertions.assertThrows(IngredientException.class, () -> ingredientServiceImpl.ajouter(tomate));
        Assertions.assertEquals("La quantité doit être supérieure ou égale à 0", ex.getMessage());
    }

    @Test
    void testAjoutIngredientNomNull() {
        IngredientRequest tomate = new IngredientRequest(null, 3);
        IngredientException ex = Assertions.assertThrows(IngredientException.class, () -> ingredientServiceImpl.ajouter(tomate));
        Assertions.assertEquals("Le nom est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutIngredientNomBlank() {
        IngredientRequest tomate = new IngredientRequest(" ", 3);
        IngredientException ex = Assertions.assertThrows(IngredientException.class, () -> ingredientServiceImpl.ajouter(tomate));
        Assertions.assertEquals("Le nom est obligatoire", ex.getMessage());
    }

    @Test
    void testAjouterOk() {
        IngredientRequest tomateRequete = new IngredientRequest("Tomate", 3);
        IngredientResponse tomateReponse = new IngredientResponse(1, "Tomate", 3);

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
        IngredientResponse tomateReponse = new IngredientResponse(1, "Tomate", 3);

        Mockito.when(ingredientDao.findById(1)).thenReturn(Optional.of(tomate));
        Mockito.when(ingredientMapperMock.toIngredientResponse(tomate)).thenReturn(tomateReponse);

        Assertions.assertEquals(tomateReponse, ingredientServiceImpl.trouverParId(1));
    }

    @DisplayName("""
            Test de la méthode afficherTousIngredients qui doit renvoyer une liste d'ingredients
            correspondant aux ingredients existant en base
            """)
    @Test
    void testAfficherTousIngredients(){
        Ingredient tomate = new Ingredient("Tomate", 3);
        Ingredient parmesan = new Ingredient("Parmesan", 7);
        List<Ingredient> ingredients = List.of(tomate, parmesan);

        IngredientResponse tomateReponse = new IngredientResponse(1, "Tomate", 3);
        IngredientResponse parmesanReponse = new IngredientResponse(2, "Parmesan", 7);
        List<IngredientResponse> dtos = List.of(tomateReponse, parmesanReponse);

        Mockito.when(ingredientDao.findAll()).thenReturn(ingredients);
        Mockito.when(ingredientMapperMock.toIngredientResponse(tomate)).thenReturn((tomateReponse));
        Mockito.when(ingredientMapperMock.toIngredientResponse(parmesan)).thenReturn((parmesanReponse));

        assertEquals(dtos, ingredientServiceImpl.afficherTousIngredients());
    }


}
