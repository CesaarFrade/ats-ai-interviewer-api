package com.cesarfrade.ats.repository;

import com.cesarfrade.ats.model.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {
}
