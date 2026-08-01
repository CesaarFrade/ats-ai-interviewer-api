package com.cesarfrade.ats.controller;

import com.cesarfrade.ats.model.Candidato;
import com.cesarfrade.ats.service.ICandidatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos") // La ruta base para todos los métodos de esta clase
@RequiredArgsConstructor
public class CandidatoController {
    private final ICandidatoService candidatoService;

    // POST: http://localhost:8080/api/candidatos
    @PostMapping
    public ResponseEntity<String> crearCandidato(@RequestBody Candidato candidato) {
        candidatoService.saveCandidato(candidato);
        return new ResponseEntity<>("¡Candidato guardado con éxito en la base de datos!", HttpStatus.CREATED);
    }

    // GET: http://localhost:8080/api/candidatos
    @GetMapping
    public ResponseEntity<List<Candidato>> obtenerTodos() {
        return new ResponseEntity<>(candidatoService.getCandidatos(), HttpStatus.OK);
    }
}
