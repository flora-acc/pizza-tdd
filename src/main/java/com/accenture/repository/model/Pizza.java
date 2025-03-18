package com.accenture.repository.model;

import com.accenture.shared.Taille;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "pizzas")
public class Pizza {
    @Id
    @GeneratedValue
    private int id;
    private String nom;

    @ManyToMany
    private List<Ingredient> ingredients;

    @ElementCollection
    @CollectionTable(name = "pizza_prix", joinColumns = @JoinColumn(name = "pizza_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "taille")// Stocke la clé de l'EnumMap sous forme de chaîne
    @Column(name = "prix") // Valeur stockée
    private Map<Taille,Double> prix;
}
