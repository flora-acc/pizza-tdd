package com.accenture.repository;

import com.accenture.repository.model.Commande;
import com.accenture.service.dto.CommandeResponseDto;
import com.accenture.shared.Statut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommandeDao extends JpaRepository<Commande, Integer> {
    List<Commande> findByStatut(Statut statut);
}
