package com.cesarfrade.ats.service;

import com.cesarfrade.ats.dto.LoginDTO;
import com.cesarfrade.ats.dto.RegistroUsuarioDTO;
import com.cesarfrade.ats.model.Candidato;
import com.cesarfrade.ats.model.Usuario;
import com.cesarfrade.ats.security.JwtUtil;
import com.cesarfrade.ats.security.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private ICandidatoService candidatoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Usuario registrarUsuario(RegistroUsuarioDTO dto) {
        if (usuarioService.existePorEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // 1. Guardamos el usuario de seguridad
        Usuario nuevoUsuario = Usuario.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .rol(dto.getRol())
                .build();

        Usuario usuarioGuardado = usuarioService.guardarUsuario(nuevoUsuario);

        // 2. Si es candidato, creamos su perfil completo automáticamente con los datos del formulario
        if (dto.getRol() == Rol.ROLE_CANDIDATO) {
            Candidato perfilCandidato = Candidato.builder()
                    .email(dto.getEmail())
                    .nombreCandidato(dto.getNombreCandidato()) // <-- ¡Guardamos el nombre!
                    .telefono(dto.getTelefono())               // <-- ¡Guardamos el teléfono!
                    .build();
            candidatoService.guardarCandidatoInterno(perfilCandidato);
        }

        return usuarioGuardado;
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