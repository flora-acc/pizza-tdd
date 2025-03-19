package com.accenture.repository.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String prenom;
    private String email;

    @ManyToMany
    List<Commande> historiqueComm;

    public Client(String nom, String prenom, String email, List<Commande> historiqueComm) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.historiqueComm = historiqueComm;
    }
}
