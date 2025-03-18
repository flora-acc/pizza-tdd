package com.accenture.service.mapper;

import com.accenture.repository.model.Ingredient;
import com.accenture.service.dto.IngredientRequest;
import com.accenture.service.dto.IngredientResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient toIngredient(IngredientRequest ingredientRequest);
    IngredientResponse toIngredientResponse(Ingredient ingredient);
    IngredientRequest toIngredientRequest(Ingredient ingredient);
}
