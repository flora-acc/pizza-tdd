package com.accenture.repository.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "commande")
public class Commande {

    @Id
    private int id;

    @ManyToOne
    private Client client;

    @OneToMany
    private List<PizzaTailleQte> pizzaTailleQteList;

    public Commande(int id) {
        this.id = id;
    }
}

