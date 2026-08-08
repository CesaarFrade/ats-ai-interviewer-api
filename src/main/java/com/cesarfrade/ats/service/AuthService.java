package com.cesarfrade.ats.service;

import com.cesarfrade.ats.dto.LoginDTO;
import com.cesarfrade.ats.dto.RegistroUsuarioDTO;
import com.cesarfrade.ats.model.Usuario;
import com.cesarfrade.ats.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Usuario registrarUsuario(RegistroUsuarioDTO dto) {
        // Usamos el servicio
        if (usuarioService.existePorEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario nuevoUsuario = Usuario.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .rol(dto.getRol())
                .build();

        // Usamos el servicio
        return usuarioService.guardarUsuario(nuevoUsuario);
    }

    public String login(LoginDTO dto) {
        // Usamos el servicio
        Usuario usuario = usuarioService.buscarPorEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtUtil.generarToken(usuario);
    }
}