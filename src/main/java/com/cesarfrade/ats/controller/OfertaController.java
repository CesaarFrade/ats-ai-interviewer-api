package com.cesarfrade.ats.controller;

import com.cesarfrade.ats.model.Oferta;
import com.cesarfrade.ats.service.IOfertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ofertas") // La ruta base para todos los métodos de esta clase
@RequiredArgsConstructor
public class OfertaController {
    private final IOfertaService ofertaService;

    // POST: http://localhost:8080/api/ofertas
    @PostMapping
    public ResponseEntity<String> crearOferta(@RequestBody Oferta oferta) {
        ofertaService.saveOferta(oferta);
        return new ResponseEntity<>("¡Oferta guardada con éxito en la base de datos!", HttpStatus.CREATED);
    }

    // GET: http://localhost:8080/api/ofertas
    @GetMapping
    public ResponseEntity<List<Oferta>> obtenerTodas() {
        return new ResponseEntity<>(ofertaService.getOfertas(), HttpStatus.OK);
    }
}

