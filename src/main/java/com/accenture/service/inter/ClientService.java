package com.accenture.service.inter;

import com.accenture.exception.ClientException;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ClientService {


    ClientResponseDto ajouterClient(ClientRequestDto clientRequestDto) throws ClientException;
    ClientResponseDto trouverParId(int id) throws EntityNotFoundException;
    List<ClientResponseDto> afficherTousClients() throws ClientException;
}
