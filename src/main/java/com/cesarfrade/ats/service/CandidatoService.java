package com.cesarfrade.ats.service;

import com.cesarfrade.ats.dto.CandidatoRequestDTO;
import com.cesarfrade.ats.dto.CandidatoResponseDTO;
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

    // Métodos CRUD
    @Override
    public CandidatoResponseDTO findCandidato(Long id_candidato) {
        Candidato candidato = candRepo.findById(id_candidato).orElse(null);
        if (candidato != null) {
            return mapToResponse(candidato);
        } else {
            throw new NotFoundException("Por el momento, no existe ningún candidato"
                    + "con el id indicado");
        }
    }

    @Override
    public List<CandidatoResponseDTO> getCandidatos() {
        return candRepo.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void saveCandidato(CandidatoRequestDTO candidatoDTO) {
        Candidato candidato = Candidato.builder()
                .nombreCandidato(candidatoDTO.getNombreCandidato())
                .email(candidatoDTO.getEmail())
                .telefono(candidatoDTO.getTelefono())
                .build();
        candRepo.save(candidato);
    }

    @Override
    public Candidato guardarCandidatoInterno(Candidato candidato) {
        return candRepo.save(candidato);
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
    public void editCandidato(CandidatoRequestDTO candidatoDTO, Long id_candidato) {
        Candidato candidatoInicial = candRepo.findById(id_candidato).orElse(null);
        if (candidatoInicial == null) {
            throw new NotFoundException("Por el momento, no existe ningún candidato"
                    + "con el id indicado");
        } else {
            if (candidatoDTO.getNombreCandidato() != null) {
                candidatoInicial.setNombreCandidato(candidatoDTO.getNombreCandidato());
            }
            if (candidatoDTO.getEmail() != null) {
                candidatoInicial.setEmail(candidatoDTO.getEmail());
            }
            if (candidatoDTO.getTelefono() != null) {
                candidatoInicial.setTelefono(candidatoDTO.getTelefono());
            }
            candRepo.save(candidatoInicial);
        }
    }

    private CandidatoResponseDTO mapToResponse(Candidato candidato) {
        return CandidatoResponseDTO.builder()
                .id(candidato.getId())
                .nombreCandidato(candidato.getNombreCandidato())
                .email(candidato.getEmail())
                .telefono(candidato.getTelefono())
                .fechaRegistro(candidato.getFechaRegistro())
                .build();
    }

    @Override
    public CandidatoResponseDTO findByEmail(String email) {
        Candidato candidato = candRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No existe ningún candidato con el email indicado: " + email));
        return mapToResponse(candidato);
    }
}
