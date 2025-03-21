package com.accenture.service.dto;


public record ClientResponseDto (
    int id,
    String prenom,
    String nom,
    String email
){
}
