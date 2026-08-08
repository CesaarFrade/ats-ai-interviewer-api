package com.cesarfrade.ats.repository;

import com.cesarfrade.ats.model.Postulacion;
import com.cesarfrade.ats.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {
    Optional<Usuario> findByEmail(String email);
}
