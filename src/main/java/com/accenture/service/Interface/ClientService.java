package com.accenture.service.Interface;

import com.accenture.exception.ClientException;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;

public interface ClientService {

    ClientResponseDto ajouterClient(ClientRequestDto clientRequestDto) throws ClientException;
}
