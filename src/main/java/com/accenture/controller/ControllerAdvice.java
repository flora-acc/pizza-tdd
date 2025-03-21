package com.accenture.controller;

import com.accenture.exception.ClientException;
import com.accenture.exception.IngredientException;
import com.accenture.exception.PizzaException;
import com.accenture.shared.ErreurReponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    public static final String ERREUR_FONCTIONNELLE = "Erreur Fonctionnelle : ";
    public static final String ERREUR = "Erreur : {}";

    @ExceptionHandler(IngredientException.class)
    public ResponseEntity<ErreurReponse> gestionIngredientException(IngredientException ex){
        ErreurReponse er = new ErreurReponse(LocalDate.now(), ERREUR_FONCTIONNELLE, ex.getMessage());
        log.error(ERREUR, er.message());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErreurReponse> gestionEntityNotFoundException(EntityNotFoundException ex){
        ErreurReponse er = new ErreurReponse(LocalDate.now(), ERREUR_FONCTIONNELLE, ex.getMessage());
        log.error(ERREUR, er.message());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(PizzaException.class)
    public ResponseEntity<ErreurReponse> gestionPizzaException(PizzaException ex) {
        ErreurReponse er = new ErreurReponse(LocalDate.now(), ERREUR_FONCTIONNELLE, ex.getMessage());
        log.error(ERREUR, er.message());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErreurReponse> gestionClientException(ClientException ex) {
        ErreurReponse er = new ErreurReponse(LocalDate.now(), ERREUR_FONCTIONNELLE, ex.getMessage());
        log.error(ERREUR, er.message());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErreurReponse> gestionClientException(HttpMessageNotReadableException ex) {
        ErreurReponse er = new ErreurReponse(LocalDate.now(), ERREUR_FONCTIONNELLE, ex.getMessage());
        log.error(ERREUR, er.message());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }
}
