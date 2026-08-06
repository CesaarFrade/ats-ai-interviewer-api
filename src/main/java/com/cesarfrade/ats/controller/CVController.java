package com.cesarfrade.ats.controller;

import com.cesarfrade.ats.dto.CVRequestDTO;
import com.cesarfrade.ats.dto.CVResponseDTO;
import com.cesarfrade.ats.service.ICVService;
import com.cesarfrade.ats.service.PdfExtractorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/cvs") // La ruta base para todos los métodos de esta clase
@RequiredArgsConstructor
public class CVController {
    private final ICVService cvService;
    private final PdfExtractorService pdfExtractorService;

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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> subirCVReal(
            @RequestParam("candidatoId") Long candidatoId,
            @RequestParam("archivoPdf") MultipartFile archivoPdf
    ) {
        // El servicio hace todo el trabajo y nos devuelve el texto
        String textoExtraido = cvService.procesarYGuardarPdf(candidatoId, archivoPdf);

        // Formateamos una respuesta amigable
        String preview = textoExtraido.substring(0, Math.min(textoExtraido.length(), 100));
        return new ResponseEntity<>("¡PDF procesado con éxito! Extracto: " + preview + "...", HttpStatus.CREATED);
    }
}
