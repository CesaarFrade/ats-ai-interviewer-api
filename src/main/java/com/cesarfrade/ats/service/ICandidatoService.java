package com.cesarfrade.ats.service;

import com.cesarfrade.ats.model.Candidato;

import java.util.List;

public interface ICandidatoService {
    public Candidato findCandidato(Long id_candidato);
    public List<Candidato> getCandidatos();
    public void saveCandidato(Candidato candidato);
    public void deleteCandidato(Long id_candidato);
    public void editCandidato(Candidato candidato, Long id_candidato);
}
