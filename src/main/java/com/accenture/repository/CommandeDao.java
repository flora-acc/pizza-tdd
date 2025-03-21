package com.accenture.repository;

import com.accenture.repository.model.Commande;

import com.accenture.shared.Statut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommandeDao extends JpaRepository<Commande, Integer> {
    List<Commande> findByStatut(Statut statut);
}
