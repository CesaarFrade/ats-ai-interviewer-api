package com.cesarfrade.ats.service;

import com.cesarfrade.ats.exception.NotFoundException;
import com.cesarfrade.ats.model.Candidato;
import com.cesarfrade.ats.repository.CandidatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidatoService implements ICandidatoService {
    private final CandidatoRepository candRepo;

    //Métodos CRUD
    @Override
    public Candidato findCandidato(Long id_candidato) {
        Candidato candidato = candRepo.findById(id_candidato).orElse(null);
        if (candidato != null) {
            return candidato;
        } else {
            throw new NotFoundException("Por el momento, no existe ningún candidato"
                    + "con el id indicado");
        }
    }

    @Override
    public List<Candidato> getCandidatos() {
        return candRepo.findAll();
    }

    @Override
    public void saveCandidato(Candidato candidato) {
        candRepo.save(candidato);
    }

    @Override
    public void deleteCandidato(Long id_candidato) {
        if (candRepo.existsById(id_candidato)) {
            candRepo.deleteById(id_candidato);
        } else {
            throw new NotFoundException("Por el momento, no existe ningún candidato"
                    + "con el id indicado");
        }
    }

    @Override
    public void editCandidato(Candidato candidato, Long id_candidato) {
        Candidato candidatoInicial = candRepo.findById(id_candidato).orElse(null);
        if (candidatoInicial == null) {
            throw new NotFoundException("Por el momento, no existe ningún candidato"
                    + "con el id indicado");
        } else {
            if (candidato.getNombreCandidato() != null) {
                candidatoInicial.setNombreCandidato(candidato.getNombreCandidato());
            }
            if (candidato.getEmail() != null) {
                candidatoInicial.setEmail(candidato.getEmail());
            }
            if (candidato.getTelefono() != null) {
                candidatoInicial.setTelefono(candidato.getTelefono());
            }
            if (candidato.getFechaRegistro() != null) {
                candidatoInicial.setFechaRegistro(candidato.getFechaRegistro());
            }
            candRepo.save(candidatoInicial);
        }
    }
}
