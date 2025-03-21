package com.accenture.service.inter;

import com.accenture.exception.CommandeException;
import com.accenture.service.dto.CommandeRequestDto;
import com.accenture.service.dto.CommandeResponseDto;
import com.accenture.shared.Statut;

import java.util.List;

public interface CommandeService {
    CommandeResponseDto ajouter(CommandeRequestDto commandeRequestDto) throws CommandeException;

    CommandeResponseDto trouverParId(int id);

    List<CommandeResponseDto> trouverToutes();

    CommandeResponseDto modifierStatus(int id, Statut statut);

    List<CommandeResponseDto> trouverParStatut(Statut status);
}
