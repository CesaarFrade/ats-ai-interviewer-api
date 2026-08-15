package com.cesarfrade.ats.repository;

import com.cesarfrade.ats.model.Entrevista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntrevistaRepository extends JpaRepository<Entrevista, Long> {
    Optional<Entrevista> findByPostulacionId(Long postulacionId);
}
