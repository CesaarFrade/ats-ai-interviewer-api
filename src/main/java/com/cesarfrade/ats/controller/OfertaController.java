package com.cesarfrade.ats.controller;

import com.cesarfrade.ats.dto.OfertaRequestDTO;
import com.cesarfrade.ats.dto.OfertaResponseDTO;
import com.cesarfrade.ats.service.IOfertaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/ofertas") // La ruta base para todos los métodos de esta clase
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OfertaController {
    private final IOfertaService ofertaService;

    // POST: http://localhost:8080/api/ofertas
    @PreAuthorize("hasRole('EMPRESA')")
    @PostMapping
    public ResponseEntity<String> crearOferta(@Valid @RequestBody OfertaRequestDTO oferta) {
        ofertaService.saveOferta(oferta);
        return new ResponseEntity<>("¡Oferta guardada con éxito en la base de datos!", HttpStatus.CREATED);
    }

    // GET: http://localhost:8080/api/ofertas
    @GetMapping
    public ResponseEntity<List<OfertaResponseDTO>> obtenerTodas() {
        return new ResponseEntity<>(ofertaService.getOfertas(), HttpStatus.OK);
    }
}

