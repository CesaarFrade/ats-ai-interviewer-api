package com.cesarfrade.ats.service;

import com.cesarfrade.ats.dto.CandidatoRequestDTO;
import com.cesarfrade.ats.dto.CandidatoResponseDTO;
import com.cesarfrade.ats.model.Candidato;

import java.util.List;

public interface ICandidatoService {
    public CandidatoResponseDTO findCandidato(Long id_candidato);
    public List<CandidatoResponseDTO> getCandidatos();
    Candidato guardarCandidatoInterno(Candidato candidato);
    public void saveCandidato(CandidatoRequestDTO candidato);
    public void deleteCandidato(Long id_candidato);
    public void editCandidato(CandidatoRequestDTO candidato, Long id_candidato);
}
