package com.cesarfrade.ats.service;

import com.cesarfrade.ats.dto.CVRequestDTO;
import com.cesarfrade.ats.dto.CVResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICVService {
    public CVResponseDTO findCV(Long id_CV);
    public List<CVResponseDTO> getCVs();
    public void saveCV(CVRequestDTO cv);
    public void deleteCV(Long id_cv);
    public void editCV(CVRequestDTO cv, Long id_cv);
    String procesarYGuardarPdf(Long candidatoId, MultipartFile archivoPdf);
}
