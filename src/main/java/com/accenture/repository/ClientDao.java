package com.accenture.repository;

import com.accenture.repository.model.Client;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientDao extends JpaRepository<Client, Integer>  {

    Optional<Client> findById(int id);
}
