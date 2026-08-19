package com.cesarfrade.ats.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PdfExtractorService {

    public String extraerTextoPdf(MultipartFile archivoPdf) {
        // PDDocument.load lee el flujo de datos del archivo directamente desde la memoria
        try (PDDocument documento = PDDocument.load(archivoPdf.getInputStream())) {

            PDFTextStripper stripper = new PDFTextStripper();
            String textoExtraido = stripper.getText(documento);

            if (textoExtraido == null || textoExtraido.trim().isEmpty()) {
                throw new IllegalArgumentException("El PDF parece estar vacío o es una imagen escaneada sin texto.");
            }

            return textoExtraido;

        } catch (IOException e) {
            throw new RuntimeException("Error inesperado al intentar procesar el PDF: " + e.getMessage());
        }
    }
}
