package com.accenture.service;

import com.accenture.exception.IngredientException;
import com.accenture.exception.PizzaException;
import com.accenture.repository.IngredientDao;

import com.accenture.repository.PizzaDao;
import com.accenture.repository.model.Ingredient;
import com.accenture.repository.model.Pizza;
import com.accenture.service.Impl.PizzaServiceImpl;
import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import com.accenture.shared.Taille;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;


@ExtendWith(MockitoExtension.class)
class PizzaServiceImplTest {

    @InjectMocks
    private PizzaServiceImpl pizzaServiceImpl;
    @Mock
    IngredientDao ingredientDaoMock;
    @Mock
    PizzaDao pizzaDaoMock;

    @Test
    void testAjoutAvecPizzaNull() {
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(null));
        Assertions.assertEquals("La pizza est null", ex.getMessage());
    }

    @Test
    void testAjoutPizzaNomNull() {
       Map<Taille, Double> mapPrix = new HashMap<>();
        mapPrix.put(Taille.PETITE, 10.00);
        mapPrix.put(Taille.MOYENNE, 12.00);
        mapPrix.put(Taille.GRANDE, 14.00);
        PizzaRequestDto pizzaRequest = new PizzaRequestDto(null, List.of(1, 2, 3), mapPrix);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Le nom de la pizza est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutPizzaNomBlank() {
       Map<Taille, Double> mapPrix = new HashMap<>();
        mapPrix.put(Taille.PETITE, 10.00);
        mapPrix.put(Taille.MOYENNE, 12.00);
        mapPrix.put(Taille.GRANDE, 14.00);
        PizzaRequestDto pizzaRequest = new PizzaRequestDto("", List.of(1, 2, 3), mapPrix);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Le nom de la pizza est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutPizzaListNull() {
       Map<Taille, Double> mapPrix = new HashMap<>();
        mapPrix.put(Taille.PETITE, 10.00);
        mapPrix.put(Taille.MOYENNE, 12.00);
        mapPrix.put(Taille.GRANDE, 14.00);
        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", null, mapPrix);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("La liste d'ingrédients est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutPizzaListInferior2() {
     Map<Taille, Double> mapPrix = new HashMap<>();
        mapPrix.put(Taille.PETITE, 10.00);
        mapPrix.put(Taille.MOYENNE, 12.00);
        mapPrix.put(Taille.GRANDE, 14.00);
        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1), mapPrix);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("La pizza doit utiliser deux ingrédients ou plus", ex.getMessage());
    }

    @Test
    void testAjoutPizzaPrixNull() {
        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1, 2), null);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("La liste des prix est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutPizzaPrixNotComplete() {
        Map<Taille, Double> mapPrix = new HashMap<>();
        mapPrix.put(Taille.PETITE, 10.00);
        mapPrix.put(Taille.MOYENNE, 12.00);

        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1, 2), mapPrix);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Tous les prix par taille sont obligatoires", ex.getMessage());
    }

    @Test
    void testAjoutPizzaIngredientIntrouvable() {
        Map<Taille, Double> mapPrix = new HashMap<>();
        mapPrix.put(Taille.PETITE, 10.00);
        mapPrix.put(Taille.MOYENNE, 12.00);
        mapPrix.put(Taille.GRANDE, 14.00);
        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1, 2), mapPrix);
        Mockito.when(ingredientDaoMock.findById(1)).thenReturn(Optional.empty());

        IngredientException ex = Assertions.assertThrows(IngredientException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Ingrédient Invalide", ex.getMessage());
    }

    @Test
    void testAjoutPizzaPrixTailleNull() {
     Map<Taille, Double> mapPrix = new HashMap<>();
        mapPrix.put(Taille.PETITE, 10.00);
        mapPrix.put(Taille.MOYENNE, null);
        mapPrix.put(Taille.GRANDE, 14.00);
        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1, 2), mapPrix);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Un prix ne doit pas être null", ex.getMessage());
    }

    @Test
    void testAjoutPizzaPrixTaille0() {
       Map<Taille, Double> mapPrix = new HashMap<>();
        mapPrix.put(Taille.PETITE, 10.00);
        mapPrix.put(Taille.MOYENNE, 0.0);
        mapPrix.put(Taille.GRANDE, 14.00);
        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1, 2), mapPrix);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Un prix ne doit pas être égal à 0", ex.getMessage());
    }

    @Test
    void AjouterMappingIngredient() {
        Map<Taille, Double> mapPrix = new HashMap<>();
        mapPrix.put(Taille.PETITE, 10.00);
        mapPrix.put(Taille.MOYENNE, 12.0);
        mapPrix.put(Taille.GRANDE, 14.00);
        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1, 2), mapPrix);


        PizzaResponseDto pizzaResponseDto = new PizzaResponseDto(1, "Trois fromages", List.of("Mozzarrela", "Ananas"), mapPrix);
        Ingredient ingredient1 = new Ingredient("Mozzarrela", 3);
        ingredient1.setId(1);
        Ingredient ingredient2 = new Ingredient("Ananas", 10);
        ingredient2.setId(2);
        List<Ingredient> ingredientList = List.of(ingredient1, ingredient2);

        Pizza pizzaAvant = new Pizza();
        pizzaAvant.setPrix(mapPrix);
        pizzaAvant.setIngredients(ingredientList);
        pizzaAvant.setNom("Trois fromages");

        Pizza pizzaApres = new Pizza();
        pizzaApres.setId(1);
        pizzaApres.setPrix(mapPrix);
        pizzaApres.setIngredients(ingredientList);
        pizzaApres.setNom("Trois fromages");


        Mockito.when(ingredientDaoMock.findById(1)).thenReturn(Optional.of(ingredient1));
        Mockito.when(ingredientDaoMock.findById(2)).thenReturn(Optional.of(ingredient2));
        Mockito.when(pizzaDaoMock.save(pizzaAvant)).thenReturn(pizzaApres);
        Assertions.assertEquals(pizzaResponseDto, pizzaServiceImpl.ajouter(pizzaRequest));

        Mockito.verify(pizzaDaoMock).save(pizzaAvant);
    }

}
