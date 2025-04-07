package com.faculdade.sala.repository;

import com.faculdade.sala.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findByDisponivel(Boolean disponivel);
} 