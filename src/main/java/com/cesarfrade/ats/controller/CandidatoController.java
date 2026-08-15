package com.cesarfrade.ats.controller;

import com.cesarfrade.ats.dto.CandidatoRequestDTO;
import com.cesarfrade.ats.dto.CandidatoResponseDTO;
import com.cesarfrade.ats.service.ICandidatoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/candidatos") // La ruta base para todos los métodos de esta clase
@RequiredArgsConstructor
public class CandidatoController {
    private final ICandidatoService candidatoService;

    // POST: http://localhost:8080/api/candidatos
    @PostMapping
    public ResponseEntity<String> crearCandidato(@Valid @RequestBody CandidatoRequestDTO candidato) {
        candidatoService.saveCandidato(candidato);
        return new ResponseEntity<>("¡Candidato guardado con éxito en la base de datos!", HttpStatus.CREATED);
    }

    // GET: http://localhost:8080/api/candidatos
    @GetMapping
    public ResponseEntity<List<CandidatoResponseDTO>> obtenerTodos() {
        return new ResponseEntity<>(candidatoService.getCandidatos(), HttpStatus.OK);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ROLE_CANDIDATO')")
    public ResponseEntity<CandidatoResponseDTO> obtenerMiPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        // userDetails.getUsername() nos da el email extraído del token JWT
        CandidatoResponseDTO candidato = candidatoService.findByEmail(userDetails.getUsername());
        return new ResponseEntity<>(candidato, HttpStatus.OK);
    }
}
