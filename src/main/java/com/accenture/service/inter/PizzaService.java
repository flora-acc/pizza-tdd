package com.accenture.service.inter;

import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import com.accenture.shared.Filtre;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface PizzaService {

    PizzaResponseDto ajouter(PizzaRequestDto pizzaRequest);

    PizzaResponseDto supprimerDeLaCarteParId(int id);

    List<PizzaResponseDto> trouverToutes(Filtre filtre);

    PizzaResponseDto trouverParId(int i);

    List<PizzaResponseDto> trouverParNom(String s);

    List<PizzaResponseDto> trouverPizzaParIdIngredient(int ingredientId) throws EntityNotFoundException;
}
