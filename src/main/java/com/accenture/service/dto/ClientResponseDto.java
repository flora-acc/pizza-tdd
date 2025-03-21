package com.accenture.service.dto;

import com.accenture.repository.model.Commande;

import java.util.List;

public record ClientResponseDto (
    int id,
    String prenom,
    String nom,
    String email
){
}
