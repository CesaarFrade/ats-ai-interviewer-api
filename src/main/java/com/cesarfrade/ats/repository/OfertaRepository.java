package com.cesarfrade.ats.repository;

import com.cesarfrade.ats.model.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {
    List<Oferta> findByCreadorEmail(String email);
}
