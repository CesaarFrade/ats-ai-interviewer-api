package com.cesarfrade.ats.controller;

import com.cesarfrade.ats.dto.LoginDTO;
import com.cesarfrade.ats.dto.RegistroUsuarioDTO;
import com.cesarfrade.ats.model.Usuario;
import com.cesarfrade.ats.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody RegistroUsuarioDTO dto) {
        Usuario nuevoUsuario = authService.registrarUsuario(dto);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto) {
        String token = authService.login(dto);
        // Devolvemos el token en formato texto plano
        return ResponseEntity.ok(token);
    }
}
