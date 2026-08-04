package com.cesarfrade.ats.repository;

import com.cesarfrade.ats.model.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {
    List<Postulacion> findByOfertaIdAndPorcentajeMatchGreaterThanEqual(Long ofertaId, Double minMatch);
}
