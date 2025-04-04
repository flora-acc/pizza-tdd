package com.accenture.service;

import com.accenture.exception.PizzaException;
import com.accenture.repository.IngredientDao;

import com.accenture.repository.PizzaDao;
import com.accenture.repository.model.Ingredient;
import com.accenture.repository.model.Pizza;
import com.accenture.service.impl.PizzaServiceImpl;
import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import com.accenture.shared.Filtre;
import com.accenture.shared.Taille;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.List;


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

        PizzaRequestDto pizzaRequest = new PizzaRequestDto(null, List.of(1, 2, 3), creationMapPrix(), true);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Le nom de la pizza est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutPizzaNomBlank() {

        PizzaRequestDto pizzaRequest = new PizzaRequestDto("", List.of(1, 2, 3), creationMapPrix(), true);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Le nom de la pizza est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutPizzaListNull() {

        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", null, creationMapPrix(), true);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("La liste d'ingrédients est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutPizzaListInferior2() {

        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1), creationMapPrix(), true);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("La pizza doit utiliser deux ingrédients ou plus", ex.getMessage());
    }

    @Test
    void testAjoutPizzaPrixNull() {
        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1, 2), null, true);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("La liste des prix est obligatoire", ex.getMessage());
    }

    @Test
    void testAjoutPizzaPrixNotComplete() {
        Map<Taille, Double> mapPrix = new EnumMap<>(Taille.class);
        mapPrix.put(Taille.PETITE, 10.00);
        mapPrix.put(Taille.MOYENNE, 12.00);

        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1, 2), mapPrix, true);

        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Tous les prix par taille sont obligatoires", ex.getMessage());
    }


    @Test
    void testAjoutPizzaPrixTailleNull() {
        Map<Taille, Double> mapPrix = creationMapPrix();

        mapPrix.put(Taille.MOYENNE, null);

        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1, 2), mapPrix, true);
        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Un prix ne doit pas être null", ex.getMessage());
    }

    @Test
    void testAjoutPizzaPrixTaille0() {
        Map<Taille, Double> mapPrix = creationMapPrix();

        mapPrix.put(Taille.MOYENNE, 0.0);

        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1, 2), mapPrix, true);

        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Un prix ne doit pas être égal à 0", ex.getMessage());
    }

    @Test
    void testAjoutPizzaCommandableNull() {
        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Trois fromages", List.of(1, 2), creationMapPrix(), null);

        PizzaException ex = Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.ajouter(pizzaRequest));
        Assertions.assertEquals("Précisez si la pizza est commandable", ex.getMessage());
    }



    @Test
    void testSupprimerDeLaCarteCommandableEntityNotFound() {
        Mockito.when(pizzaDaoMock.findById(1)).thenReturn(Optional.empty());
        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class, () -> pizzaServiceImpl.supprimerDeLaCarteParId(1));
        Assertions.assertEquals("Aucune Pizza à cette id", ex.getMessage());
    }

    @Test
    void testSupprimerDeLaCarteCommandable() {
        Pizza pizza = troisFromages();
        pizza.setCommandable(true);
        Pizza pizza1 = troisFromages();
        pizza1.setCommandable(false);
        Mockito.when(pizzaDaoMock.findById(1)).thenReturn(Optional.of(pizza));
        Mockito.when(pizzaDaoMock.save(pizza)).thenReturn(pizza1);
        Assertions.assertEquals(false, pizzaServiceImpl.supprimerDeLaCarteParId(1).commandable());
    }

    @Test
    void testTrouverToutesCOMMANDABLE() {
        Pizza pizza1 = troisFromages();
        pizza1.setId(1);
        Pizza pizza2 = hawainne();
        pizza2.setId(2);
        List<Pizza> listePizza = List.of(pizza1, pizza2);

        PizzaResponseDto pizzaResponseDto = new PizzaResponseDto(1, "Trois fromages", List.of("Mozzarrela", "Ananas"), creationMapPrix(), true);
        PizzaResponseDto pizzaResponseDto2 = new PizzaResponseDto(2, "Hawainne", List.of("Mozzarrela", "Ananas"), creationMapPrix(), true);

        List<PizzaResponseDto> listResponse = List.of(pizzaResponseDto, pizzaResponseDto2);

        Mockito.when(pizzaDaoMock.findByCommandableTrue()).thenReturn(listePizza);

        Assertions.assertEquals(listResponse, pizzaServiceImpl.trouverToutes(Filtre.COMMANDABLE));

    }

    @Test
    void testTrouverToutesRETIREE() {
        Pizza pizza1 = troisFromages();
        pizza1.setId(1);
        pizza1.setCommandable(false);
        Pizza pizza2 = hawainne();
        pizza2.setId(2);
        pizza2.setCommandable(false);
        List<Pizza> listPizza = List.of(pizza1, pizza2);

        PizzaResponseDto pizzaResponseDto = new PizzaResponseDto(1, "Trois fromages", List.of("Mozzarrela", "Ananas"), creationMapPrix(), false);
        PizzaResponseDto pizzaResponseDto2 = new PizzaResponseDto(2, "Hawainne", List.of("Mozzarrela", "Ananas"), creationMapPrix(), false);

        List<PizzaResponseDto> listResponse = List.of(pizzaResponseDto, pizzaResponseDto2);

        Mockito.when(pizzaDaoMock.findByCommandableFalse()).thenReturn(listPizza);

        Assertions.assertEquals(listResponse, pizzaServiceImpl.trouverToutes(Filtre.RETIREE));

    }
    @Test
    void testTrouverToutes() {
        Pizza pizza1 = troisFromages();
        pizza1.setId(1);
        pizza1.setCommandable(false);
        Pizza pizza2 = hawainne();
        pizza2.setId(2);
        pizza2.setCommandable(false);
        List<Pizza> listPizza = List.of(pizza1, pizza2);

        PizzaResponseDto pizzaResponseDto = new PizzaResponseDto(1, "Trois fromages", List.of("Mozzarrela", "Ananas"), creationMapPrix(), false);
        PizzaResponseDto pizzaResponseDto2 = new PizzaResponseDto(2, "Hawainne", List.of("Mozzarrela", "Ananas"), creationMapPrix(), false);

        List<PizzaResponseDto> listResponse = List.of(pizzaResponseDto, pizzaResponseDto2);

        Mockito.when(pizzaDaoMock.findAll()).thenReturn(listPizza);

        Assertions.assertEquals(listResponse, pizzaServiceImpl.trouverToutes(null));

    }

    @Test
    void testTrouverParIdIncorrect(){
        Mockito.when(pizzaDaoMock.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class, () -> pizzaServiceImpl.trouverParId(1));
        Assertions.assertEquals("Aucune Pizza à cet ID", ex.getMessage());
    }
    @Test
    void testTrouverParId(){
        Pizza pizza = troisFromages();
        pizza.setId(1);
        PizzaResponseDto pizzaResponseDto = new PizzaResponseDto(1, "Trois fromages", List.of("Mozzarrela", "Ananas"), creationMapPrix(), true);
        Mockito.when(pizzaDaoMock.findById(1)).thenReturn(Optional.of(pizza));

        Assertions.assertEquals(pizzaResponseDto, pizzaServiceImpl.trouverParId(1));

    }

    @Test
    void testTrouverparNomBlank(){
  PizzaException ex =  Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.trouverParNom(""));
  Assertions.assertEquals("La recherche ne doit pas être blank", ex.getMessage());
    }

    @Test
    void testTrouverparNomNull(){
  PizzaException ex =  Assertions.assertThrows(PizzaException.class, () -> pizzaServiceImpl.trouverParNom(null));
  Assertions.assertEquals("La recherche ne doit pas être blank", ex.getMessage());
    }


    @Test
    void testTrouverParNom(){
        Pizza pizza1 = troisFromages();
        pizza1.setId(1);
        pizza1.setCommandable(true);
        Pizza pizza2 = hawainne();
        pizza2.setNom("Trois Hawainne");
        pizza2.setId(2);
        pizza2.setCommandable(true);
        List<Pizza> listPizza = List.of(pizza1, pizza2);

        PizzaResponseDto pizzaResponseDto = new PizzaResponseDto(1, "Trois fromages", List.of("Mozzarrela", "Ananas"), creationMapPrix(), true);
        PizzaResponseDto pizzaResponseDto2 = new PizzaResponseDto(2, "Trois Hawainne", List.of("Mozzarrela", "Ananas"), creationMapPrix(), true);

        List<PizzaResponseDto> listResponse = List.of(pizzaResponseDto, pizzaResponseDto2);

        Mockito.when(pizzaDaoMock.findByNomContaining("Trois")).thenReturn(listPizza);

        Assertions.assertEquals(listResponse, pizzaServiceImpl.trouverParNom("Trois"));
    }


    @Test
    void testTrouverPizzaParIdIngredientIncorrect(){
        Mockito.when(ingredientDaoMock.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class, () -> pizzaServiceImpl.trouverPizzaParIdIngredient(1));
        Assertions.assertEquals("Aucun ingrédient trouvé avec cet ID", ex.getMessage());
    }

    @Test
    void testTrouverPizzaParIdIngredient(){
        Ingredient ingredient = new Ingredient("Mozzarrela", 3);
        ingredient.setId(1);
        Pizza pizza1 = troisFromages();
        pizza1.setId(1);
        Pizza pizza2 = hawainne();
        pizza2.setId(2);

        List<Pizza> pizzas = List.of(pizza1,pizza2);

        PizzaResponseDto pizzaResponseDto1 = new PizzaResponseDto(1, "Trois fromages", List.of("Mozzarrela", "Ananas"), creationMapPrix(), true);
        PizzaResponseDto pizzaResponseDto2 = new PizzaResponseDto(2, "Hawainne", List.of("Mozzarrela", "Ananas"), creationMapPrix(), true);

        List<PizzaResponseDto> pizzasDtos = List.of(pizzaResponseDto1,pizzaResponseDto2);
        Mockito.when(ingredientDaoMock.findById(1)).thenReturn(Optional.of(ingredient));
        Mockito.when(pizzaDaoMock.findByIngredientsId(1)).thenReturn(pizzas);

        Assertions.assertEquals(pizzasDtos, pizzaServiceImpl.trouverPizzaParIdIngredient(1));
    }



    //*************************************************************************
//    ************************ METHODES PRIVEES *******************************
//    *************************************************************************

    private static List<Ingredient> ingredients() {
        Ingredient ingredient1 = new Ingredient("Mozzarrela", 3);
        ingredient1.setId(1);
        Ingredient ingredient2 = new Ingredient("Ananas", 10);
        ingredient2.setId(2);
        return List.of(ingredient1, ingredient2);
    }

    private static Map<Taille, Double> creationMapPrix() {
        Map<Taille, Double> mapPrix = new EnumMap<>(Taille.class);
        mapPrix.put(Taille.PETITE, 10.00);
        mapPrix.put(Taille.MOYENNE, 12.0);
        mapPrix.put(Taille.GRANDE, 14.00);
        return mapPrix;
    }

    private static Pizza troisFromages() {
        Pizza pizza = new Pizza();
        pizza.setPrix(creationMapPrix());
        pizza.setIngredients(ingredients());
        pizza.setNom("Trois fromages");
        pizza.setCommandable(true);
        return pizza;
    }

    private static Pizza hawainne() {
        Pizza pizza = new Pizza();
        pizza.setPrix(creationMapPrix());
        pizza.setIngredients(ingredients());
        pizza.setNom("Hawainne");
        pizza.setCommandable(true);
        return pizza;
    }
}
