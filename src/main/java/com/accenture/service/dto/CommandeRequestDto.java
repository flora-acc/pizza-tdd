package com.accenture.service.dto;

import java.util.List;

public record CommandeRequestDto(
        Integer idClient,

        List<PizzaTailleQteRequestDto> pizzaTailleQteList
) {

}
