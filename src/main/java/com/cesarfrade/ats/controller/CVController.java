package com.cesarfrade.ats.controller;

import com.cesarfrade.ats.model.CV;
import com.cesarfrade.ats.service.ICVService;
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
    public ResponseEntity<String> crearCV(@RequestBody CV cv) {
        cvService.saveCV(cv);
        return new ResponseEntity<>("¡CV guardado con éxito en la base de datos!", HttpStatus.CREATED);
    }

    // GET: http://localhost:8080/api/cvs
    @GetMapping
    public ResponseEntity<List<CV>> obtenerTodos() {
        return new ResponseEntity<>(cvService.getCVs(), HttpStatus.OK);
    }
}
