package com.accenture.repository.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "commande")
public class Commande {

    @Id
    private int id;

    public Commande(int id) {
        this.id = id;
    }
}

