package com.cesarfrade.ats.controller; // Ajusta tu paquete

import com.cesarfrade.ats.dto.OfertaRequestDTO;
import com.cesarfrade.ats.dto.OfertaResponseDTO;
import com.cesarfrade.ats.service.IOfertaService;
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
@RequestMapping("/api/ofertas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OfertaController {

    private final IOfertaService ofertaService;

    // POST: http://localhost:8080/api/ofertas
    @PreAuthorize("hasAuthority('ROLE_EMPRESA')") // O hasRole según lo tengas en SecurityConfig
    @PostMapping
    public ResponseEntity<String> crearOferta(
            @Valid @RequestBody OfertaRequestDTO oferta,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Extraemos el email del token del usuario autenticado
        String emailEmpresa = userDetails.getUsername();

        // Le pasamos el email al servicio para que asocie la oferta a este creador
        ofertaService.saveOferta(oferta, emailEmpresa);

        return new ResponseEntity<>("¡Oferta guardada con éxito en la base de datos!", HttpStatus.CREATED);
    }

    // GET: http://localhost:8080/api/ofertas
    @PreAuthorize("hasAuthority('ROLE_EMPRESA')")
    @GetMapping
    public ResponseEntity<List<OfertaResponseDTO>> obtenerMisOfertas(@AuthenticationPrincipal UserDetails userDetails) {

        // Extraemos el email
        String emailEmpresa = userDetails.getUsername();

        // Pedimos al servicio solo las ofertas de esta empresa
        return new ResponseEntity<>(ofertaService.getOfertasByEmpresa(emailEmpresa), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CANDIDATO')") // Permitimos a los candidatos
    @GetMapping("/todas")
    public ResponseEntity<List<OfertaResponseDTO>> obtenerTodasLasOfertas() {
        // Devuelve todas las ofertas activas de la base de datos
        return new ResponseEntity<>(ofertaService.getOfertas(), HttpStatus.OK);
    }
}

