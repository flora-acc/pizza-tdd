package com.accenture.controller;


import com.accenture.repository.model.Ingredient;
import com.accenture.service.dto.IngredientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.MethodName.class)
class IngredientControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testAjouterIngredient() throws Exception {
        IngredientRequest ingredientRequest = new IngredientRequest("Tomate", 3);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/ingredients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ingredientRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nom").value("Tomate"))
                .andExpect(jsonPath("$.quantite").value(3));
    }

    @Test
    void testAjouterIngredientIncorrect() throws Exception {
        IngredientRequest ingredientRequest = new IngredientRequest("Tomate", -4);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/ingredients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ingredientRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La quantité doit être supérieure ou égale à 0"));
    }

    @Test
    void atestAfficherTousIngredients() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/ingredients"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    void atestTrouverParIdCorrect() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/ingredients/1")
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Emmental"));
    }

    @Test
    void testTrouverParIdIncorrect() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/ingredients/6")
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("L'id n'existe pas en base"));
    }

    @Test
    void testModifierPartiellementCorrect() throws Exception {
        IngredientRequest ingredientRequest = new IngredientRequest("Tomate", 3);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/ingredients/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ingredientRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Tomate"))
                .andExpect(jsonPath("$.quantite").value(3));
    }


    @Test
    void testModifierPartiellementIdIncorrect() throws Exception {
        IngredientRequest ingredientRequest = new IngredientRequest("Tomate", 3);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/ingredients/6")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ingredientRequest))

                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cet ingrédient est introuvable"));
    }
}
