package com.cesarfrade.ats.repository;

import com.cesarfrade.ats.model.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CVRepository extends JpaRepository<CV, Long> {
    List<CV> findByCandidatoId(Long candidatoId);
}
