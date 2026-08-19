package com.cesarfrade.ats.controller;

import com.cesarfrade.ats.dto.CVRequestDTO;
import com.cesarfrade.ats.dto.CVResponseDTO;
import com.cesarfrade.ats.service.ICVService;
import com.cesarfrade.ats.service.PdfExtractorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*")
@RequestMapping("/api/cvs")
@RequiredArgsConstructor
public class CVController {
    private final ICVService cvService;

    // POST: /api/cvs
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

    @PreAuthorize("hasRole('CANDIDATO')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> subirCVReal(
            @RequestParam("archivoPdf") MultipartFile archivoPdf,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Obtenemos el email del usuario de forma 100% segura (nadie puede falsificarlo)
        String email = userDetails.getUsername();

        // El servicio hace todo el trabajo y nos devuelve el texto
        String textoExtraido = cvService.procesarYGuardarPdf(email, archivoPdf);

        // Formateamos una respuesta amigable
        String preview = textoExtraido.substring(0, Math.min(textoExtraido.length(), 100));
        return new ResponseEntity<>("¡PDF procesado con éxito! Extracto: " + preview + "...", HttpStatus.CREATED);
    }
}
