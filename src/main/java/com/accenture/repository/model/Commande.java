package com.accenture.repository.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "pizzaTailleQteList")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Client client;


    @OneToMany (cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PizzaTailleQte> pizzaTailleQteList;

    public Commande(int id) {
        this.id = id;
    }
}

