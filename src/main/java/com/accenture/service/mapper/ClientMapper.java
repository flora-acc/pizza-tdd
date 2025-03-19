package com.accenture.service.mapper;

import com.accenture.repository.model.Client;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toClient(ClientRequestDto clientRequest);
    ClientResponseDto toClientResponseDto(Client client);
    ClientRequestDto toClientRequestDto(Client client);
}
