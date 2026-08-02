package com.cesarfrade.ats.controller;

import com.cesarfrade.ats.dto.CVRequestDTO;
import com.cesarfrade.ats.dto.CVResponseDTO;
import com.cesarfrade.ats.service.ICVService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cvs") // La ruta base para todos los métodos de esta clase
@RequiredArgsConstructor
public class CVController {
    private final ICVService cvService;

    // POST: http://localhost:8080/api/cvs
    @PostMapping
    public ResponseEntity<String> crearCV(@Valid @RequestBody CVRequestDTO cv) {
        cvService.saveCV(cv);
        return new ResponseEntity<>("¡CV guardado con éxito en la base de datos!", HttpStatus.CREATED);
    }

    // GET: http://localhost:8080/api/cvs
    @GetMapping
    public ResponseEntity<List<CVResponseDTO>> obtenerTodos() {
        return new ResponseEntity<>(cvService.getCVs(), HttpStatus.OK);
    }
}
