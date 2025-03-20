package com.accenture.repository.model;

import com.accenture.shared.Taille;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PizzaTailleQte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Pizza pizza;

    private Taille taille;
    private Integer quantite;

    public PizzaTailleQte(Pizza pizza, Taille taille, Integer quantite) {
        this.pizza = pizza;
        this.taille = taille;
        this.quantite = quantite;
    }
}
