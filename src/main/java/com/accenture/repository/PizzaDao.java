package com.accenture.repository;

import com.accenture.repository.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaDao extends JpaRepository<Pizza, Integer> {
}
