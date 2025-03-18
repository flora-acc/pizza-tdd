package com.accenture.repository.model;

import com.accenture.shared.Taille;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pizza {
    @Id
    @GeneratedValue
    private int id;
    private String nom;
    @ManyToMany
    private List<Ingredient> ingredients;

    private EnumMap<Taille,Double> prix;
}
