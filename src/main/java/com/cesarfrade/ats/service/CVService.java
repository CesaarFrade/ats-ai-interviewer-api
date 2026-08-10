package com.cesarfrade.ats.service;

import com.cesarfrade.ats.dto.CVRequestDTO;
import com.cesarfrade.ats.dto.CVResponseDTO;
import com.cesarfrade.ats.exception.NotFoundException;
import com.cesarfrade.ats.model.CV;
import com.cesarfrade.ats.model.Candidato;
import com.cesarfrade.ats.repository.CVRepository;
import com.cesarfrade.ats.repository.CandidatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CVService implements ICVService {
    private final CVRepository cvRepo;
    private final CandidatoRepository candRepo;
    private final PdfExtractorService pdfExtractorService;

    //Métodos CRUD
    @Override
    public CVResponseDTO findCV(Long id_CV) {
        CV cv = cvRepo.findById(id_CV).orElse(null);
        if (cv != null) {
            return mapToResponse(cv);
        } else {
            throw new NotFoundException("Por el momento, no existe ningún currículum"
                    + "con el id indicado");
        }
    }

    @Override
    public List<CVResponseDTO> getCVs() {
        return cvRepo.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void saveCV(CVRequestDTO cvDTO) {
        Candidato candidato = candRepo.findById(cvDTO.getCandidatoId()).orElse(null);
        if (candidato == null) {
            throw new NotFoundException("Por el momento, no existe ningún candidato"
                    + "con el id indicado");
        }
        CV cv = CV.builder()
                .candidato(candidato)
                .ruta_archivo_S3(cvDTO.getRutaArchivoS3())
                .texto_crudo(cvDTO.getTextoCrudo())
                .build();
        cvRepo.save(cv);
    }

    @Override
    public void deleteCV(Long id_cv) {
        if (cvRepo.existsById(id_cv)) {
            cvRepo.deleteById(id_cv);
        } else {
            throw new NotFoundException("Por el momento, no existe ningún currículum"
                    + "con el id indicado");
        }
    }

    @Override
    public void editCV(CVRequestDTO cvDTO, Long id_cv) {
        CV cvInicial = cvRepo.findById(id_cv).orElse(null);
        if (cvInicial == null) {
            throw new NotFoundException("Por el momento, no existe ningún currículum"
                    + "con el id indicado");
        } else {
            if (cvDTO.getCandidatoId() != null) {
                Candidato candidato = candRepo.findById(cvDTO.getCandidatoId()).orElse(null);
                if (candidato == null) {
                    throw new NotFoundException("Por el momento, no existe ningún candidato"
                            + "con el id indicado");
                }
                cvInicial.setCandidato(candidato);
            }
            if (cvDTO.getRutaArchivoS3() != null) {
                cvInicial.setRuta_archivo_S3(cvDTO.getRutaArchivoS3());
            }
            if (cvDTO.getTextoCrudo() != null) {
                cvInicial.setTexto_crudo(cvDTO.getTextoCrudo());
            }
            cvRepo.save(cvInicial);
        }
    }

    @Override
    public String procesarYGuardarPdf(String email, MultipartFile archivoPdf) {
        // 1. Buscamos al candidato por su email en lugar de por ID
        Candidato candidato = candRepo.findByEmail(email).orElse(null);
        if (candidato == null) {
            // Mensaje más claro para el usuario
            throw new NotFoundException("No se encontró ningún perfil de candidato asociado a este usuario.");
        }

        // 2. Extraemos el texto usando nuestra maquinaria
        String textoExtraido = pdfExtractorService.extraerTextoPdf(archivoPdf);

        // 3. Guardamos en base de datos
        CV nuevoCv = CV.builder()
                .candidato(candidato)
                .ruta_archivo_S3(archivoPdf.getOriginalFilename())
                .texto_crudo(textoExtraido)
                .build();

        cvRepo.save(nuevoCv);

        // Devolvemos el texto para que el controlador decida qué hacer con él
        return textoExtraido;
    }

    private CVResponseDTO mapToResponse(CV cv) {
        return CVResponseDTO.builder()
                .id(cv.getId())
                .candidatoId(cv.getCandidato() != null ? cv.getCandidato().getId() : null)
                .rutaArchivoS3(cv.getRuta_archivo_S3())
                .textoCrudo(cv.getTexto_crudo())
                .build();
    }
}
