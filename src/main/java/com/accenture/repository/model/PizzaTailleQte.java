package com.accenture.repository.model;

import com.accenture.shared.Taille;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PizzaTailleQte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Pizza pizza;
    private Taille taille;
    private Integer quantite;

    public PizzaTailleQte(Pizza pizza, Taille taille, Integer quantite) {
        this.pizza = pizza;
        this.taille = taille;
        this.quantite = quantite;
    }
}
