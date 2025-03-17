package com.accenture.controller;

import com.accenture.exception.IngredientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
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
}
