package com.accenture.controller;

import com.accenture.exception.IngredientException;
import com.accenture.shared.ErreurReponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(IngredientException.class)
    public ResponseEntity<ErreurReponse> gestionIngredientException(IngredientException ex){
        ErreurReponse er = new ErreurReponse(LocalDate.now(), "Erreur Fonctionnelle : ", ex.getMessage());
        log.error("Erreur : {}", er.message());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErreurReponse> gestionEntityNotFoundException(EntityNotFoundException ex){
        ErreurReponse er = new ErreurReponse(LocalDate.now(), "Erreur Fonctionnelle : ", ex.getMessage());
        log.error("Erreur : {}", er.message());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }
}
