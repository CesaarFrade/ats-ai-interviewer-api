package com.cesarfrade.ats.controller;

import com.cesarfrade.ats.model.Entrevista;
import com.cesarfrade.ats.service.EntrevistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map; // <-- Importante

@RestController
@RequestMapping("/api/entrevistas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // <-- ¡FUNDAMENTAL PARA QUE EL FRONTEND PUEDA ENVIAR EL MENSAJE!
public class EntrevistaController {

    private final EntrevistaService entrevistaService;

    @PostMapping("/iniciar/{postulacionId}")
    @PreAuthorize("hasAnyRole('EMPRESA', 'CANDIDATO')")
    public ResponseEntity<Entrevista> iniciarEntrevista(@PathVariable Long postulacionId) {
        Entrevista entrevista = entrevistaService.iniciarEntrevista(postulacionId);
        return ResponseEntity.ok(entrevista);
    }

    // EL ENDPOINT QUE FALLABA
    @PostMapping("/{entrevistaId}/mensaje")
    @PreAuthorize("hasAnyAuthority('ROLE_CANDIDATO')")
    public ResponseEntity<String> enviarMensaje(@PathVariable Long entrevistaId, @RequestBody Map<String, String> payload) {
        // Recibimos un JSON y extraemos el texto
        String mensaje = payload.get("mensaje");
        String respuestaIa = entrevistaService.enviarMensaje(entrevistaId, mensaje);
        return ResponseEntity.ok(respuestaIa);
    }

    @GetMapping("/postulacion/{postulacionId}")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPRESA', 'ROLE_CANDIDATO')")
    public ResponseEntity<Entrevista> obtenerEntrevista(@PathVariable Long postulacionId) {
        return entrevistaService.obtenerPorPostulacion(postulacionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
