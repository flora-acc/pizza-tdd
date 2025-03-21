package com.accenture.repository.model;

import com.accenture.shared.Statut;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Client client;

    private LocalDate date;
    @Enumerated(value = EnumType.STRING)
    private Statut statut;

    private Double prix;


    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PizzaTailleQte> pizzaTailleQteList;

    public Commande(int id) {
        this.id = id;
    }



    public Double calculerPrix() {
        return getPizzaTailleQteList()
                .stream()
                .mapToDouble(PizzaTailleQte::prixParPizza)
                .sum();
    }


}

