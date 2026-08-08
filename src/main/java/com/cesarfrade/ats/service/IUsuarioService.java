package com.cesarfrade.ats.service;

import com.cesarfrade.ats.model.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUsuarioService extends UserDetailsService {
    boolean existePorEmail(String email);
    Usuario guardarUsuario(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
}
