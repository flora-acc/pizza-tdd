package com.accenture.controller;

import com.accenture.service.dto.ClientRequestDto;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.MethodName.class)
class ClientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testAjouterClient() throws Exception {
        ClientRequestDto clientRequest = new ClientRequestDto("Test", "Test","test@email.fr");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.prenom").value("Test"))
                .andExpect(jsonPath("$.nom").value("Test"))
                .andExpect(jsonPath("$.email").value("test@email.fr"));
    }

    @Test
    void testAjouterClientIncorrect() throws Exception {
        ClientRequestDto clientRequest = new ClientRequestDto(" ", "Test","email@test.fr");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clientRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Le prenom est obligatoire"));
    }

    @Test
    void atestTrouverParIdCorrect() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/clients/1")
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Michel"))
                .andExpect(jsonPath("$.prenom").value("Durand"))
                .andExpect(jsonPath("$.email").value("michel@email.fr"));
    }

    @Test
    void testTrouverParIdIncorrect() throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/clients/6")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("L'id n'existe pas en base"));
    }

    @Test
    void atestAfficherTousClients() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/clients"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }
}
