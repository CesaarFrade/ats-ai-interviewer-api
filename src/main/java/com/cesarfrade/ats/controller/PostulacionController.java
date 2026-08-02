package com.cesarfrade.ats.controller;

import com.cesarfrade.ats.dto.PostulacionRequestDTO;
import com.cesarfrade.ats.dto.PostulacionResponseDTO;
import com.cesarfrade.ats.service.IPostulacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postulaciones") // La ruta base para todos los métodos de esta clase
@RequiredArgsConstructor
public class PostulacionController {
    private final IPostulacionService postulacionService;

    // POST: http://localhost:8080/api/postulaciones
    @PostMapping
    public ResponseEntity<String> crearPostulacion(@Valid @RequestBody PostulacionRequestDTO postulacion) {
        postulacionService.savePostulacion(postulacion);
        return new ResponseEntity<>("¡Postulación guardada con éxito en la base de datos!", HttpStatus.CREATED);
    }

    // GET: http://localhost:8080/api/postulaciones
    @GetMapping
    public ResponseEntity<List<PostulacionResponseDTO>> obtenerTodas() {
        return new ResponseEntity<>(postulacionService.getPostulaciones(), HttpStatus.OK);
    }
}
