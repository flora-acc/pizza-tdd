package com.accenture.service.mapper;

import com.accenture.repository.model.Ingredient;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient toIngredient(IngredientRequestDto ingredientRequest);
    IngredientResponseDto toIngredientResponse(Ingredient ingredient);
    IngredientRequestDto toIngredientRequest(Ingredient ingredient);
}
