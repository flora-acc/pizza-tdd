package com.accenture.service.Impl;

import com.accenture.exception.ClientException;
import com.accenture.repository.ClientDao;
import com.accenture.service.Interface.ClientService;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.mapper.ClientMapper;
import com.accenture.utils.Regex;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    public static final String ERREUR_VERIFICATION_CLIENT = "Erreur verification Client : {}";
    private ClientDao clientDao;
    private ClientMapper clientMapper;

    @Override
    public ClientResponseDto ajouterClient(ClientRequestDto clientRequestDto) throws ClientException {
        verificationClient(clientRequestDto);
        verifierEmail(clientRequestDto);
        return clientMapper.toClientResponseDto(
                clientDao.save(
                        clientMapper.toClient(clientRequestDto)
                )
        );
    }

    //    *************************************************************************
    //    ************************ METHODES PRIVEES *******************************
    //    *************************************************************************

    private static void verificationClient(ClientRequestDto clientRequest) {
        String message = "";
        if (clientRequest == null) {
            message = "Le client est introuvable";
            log.error(ERREUR_VERIFICATION_CLIENT, message);
            throw new ClientException(message);
        }
        if (clientRequest.nom() == null || clientRequest.nom().isBlank()) {
            message = "Le nom est obligatoire";
            log.error(ERREUR_VERIFICATION_CLIENT, message);
            throw new ClientException(message);
        }

        if (clientRequest.prenom() == null || clientRequest.prenom().isBlank()) {
            message = "Le prenom est obligatoire";
            log.error(ERREUR_VERIFICATION_CLIENT, message);
            throw new ClientException(message);
        }

        if (clientRequest.email() == null || clientRequest.email().isBlank()) {
            message = "L'email est obligatoire";
            log.error(ERREUR_VERIFICATION_CLIENT, message);
            throw new ClientException(message);
        }
    }

    private void verifierEmail(ClientRequestDto clientRequestDto) throws ClientException {
        // Vérification du format email avec une regex
        if (!clientRequestDto.email().matches(Regex.EMAIL_REGEX))
            throw new ClientException("Format de l'email invalide");
    }
}
