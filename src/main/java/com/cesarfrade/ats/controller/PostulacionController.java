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

    @GetMapping("/empresa/ofertas/{ofertaId}/candidatos")
    public ResponseEntity<List<PostulacionResponseDTO>> obtenerCandidatosFiltrados(
            @PathVariable Long ofertaId,
            @RequestParam(defaultValue = "0.0") Double minMatch
    ) {
        // Usamos el nombre del método del Servicio y quitamos el paréntesis extra
        List<PostulacionResponseDTO> lista = postulacionService.getPostulacionesParaEmpresa(ofertaId, minMatch);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    // DELETE: http://localhost:8080/api/postulaciones/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPostulacion(@PathVariable Long id) {
        postulacionService.deletePostulacion(id);
        return new ResponseEntity<>("¡Postulación eliminada con éxito de la base de datos!", HttpStatus.OK);
    }
}

