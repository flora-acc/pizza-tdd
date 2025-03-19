package com.accenture.controller;

import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.shared.Filtre;
import com.accenture.shared.Taille;
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

import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.MethodName.class)
class PizzaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void ajouterPizza() throws Exception {
        HashMap<Taille, Double> prix = new HashMap<>();
        prix.put(Taille.PETITE, 10.00);
        prix.put(Taille.MOYENNE, 12.00);
        prix.put(Taille.GRANDE, 14.00);
        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Margarita", List.of(1, 2), prix, true);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/pizzas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pizzaRequest))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void ajouterPizzaErreur() throws Exception {

        PizzaRequestDto pizzaRequest = new PizzaRequestDto("Margarita", List.of(1, 2), null, true);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/pizzas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pizzaRequest))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La liste des prix est obligatoire"));
    }

    @Test
    void btrouverToutes () throws Exception
   {
       mockMvc.perform(MockMvcRequestBuilders.get("/pizzas")).
               andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void btrouverToutesCommandables() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/pizzas?filtre=COMMANDABLE")).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void btrouverToutesRetiree() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/pizzas?filtre=RETIREE")).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void supprimerPizza() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/pizzas?id="+1)

                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.commandable").value(false));
    }

@Test
    void supprimerPizzaException() throws Exception
{
    mockMvc.perform(
                    MockMvcRequestBuilders.delete("/pizzas?id="+6)

            ).andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Aucune Pizza à cette id"));
}
}
