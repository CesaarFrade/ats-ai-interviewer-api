package com.cesarfrade.ats.service;

import com.cesarfrade.ats.dto.PostulacionRequestDTO;
import com.cesarfrade.ats.dto.PostulacionResponseDTO;
import com.cesarfrade.ats.exception.NotFoundException;
import com.cesarfrade.ats.model.Candidato;
import com.cesarfrade.ats.model.Oferta;
import com.cesarfrade.ats.model.Postulacion;
import com.cesarfrade.ats.repository.CandidatoRepository;
import com.cesarfrade.ats.repository.OfertaRepository;
import com.cesarfrade.ats.repository.PostulacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostulacionService implements IPostulacionService {
    private final PostulacionRepository postRepo;
    private final CandidatoRepository candRepo;
    private final OfertaRepository ofeRepo;

    //Métodos CRUD
    @Override
    public PostulacionResponseDTO findPostulacion(Long id_Postulacion) {
        Postulacion postulacion = postRepo.findById(id_Postulacion).orElse(null);
        if (postulacion != null) {
            return mapToResponse(postulacion);
        } else {
            throw new NotFoundException("Por el momento, no existe ninguna postulación"
                    + "con el id indicado");
        }
    }

    @Override
    public List<PostulacionResponseDTO> getPostulaciones() {
        return postRepo.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void savePostulacion(PostulacionRequestDTO postulacionDTO) {
        Candidato candidato = candRepo.findById(postulacionDTO.getCandidatoId()).orElse(null);
        if (candidato == null) {
            throw new NotFoundException("Por el momento, no existe ningún candidato"
                    + "con el id indicado");
        }
        Oferta oferta = ofeRepo.findById(postulacionDTO.getOfertaId()).orElse(null);
        if (oferta == null) {
            throw new NotFoundException("Por el momento, no existe ninguna oferta"
                    + "con el id indicado");
        }
        Postulacion postulacion = Postulacion.builder()
                .candidato(candidato)
                .oferta(oferta)
                .build();
        postRepo.save(postulacion);
    }

    @Override
    public void deletePostulacion(Long id_postulacion) {
        if (postRepo.existsById(id_postulacion)) {
            postRepo.deleteById(id_postulacion);
        } else {
            throw new NotFoundException("Por el momento, no existe ninguna postulación"
                    + "con el id indicado");
        }
    }

    @Override
    public void editPostulacion(PostulacionRequestDTO postulacionDTO, Long id_postulacion) {
        Postulacion postulacionInicial = postRepo.findById(id_postulacion).orElse(null);
        if (postulacionInicial == null) {
            throw new NotFoundException("Por el momento, no existe ninguna postulación"
                    + "con el id indicado");
        } else {
            if (postulacionDTO.getCandidatoId() != null) {
                Candidato candidato = candRepo.findById(postulacionDTO.getCandidatoId()).orElse(null);
                if (candidato == null) {
                    throw new NotFoundException("Por el momento, no existe ningún candidato"
                            + "con el id indicado");
                }
                postulacionInicial.setCandidato(candidato);
            }
            if (postulacionDTO.getOfertaId() != null) {
                Oferta oferta = ofeRepo.findById(postulacionDTO.getOfertaId()).orElse(null);
                if (oferta == null) {
                    throw new NotFoundException("Por el momento, no existe ninguna oferta"
                            + "con el id indicado");
                }
                postulacionInicial.setOferta(oferta);
            }
            if (postulacionDTO.getPorcentajeMatch() != null) {
                postulacionInicial.setPorcentajeMatch(postulacionDTO.getPorcentajeMatch());
            }
            if (postulacionDTO.getResumenIa() != null) {
                postulacionInicial.setResumenIa(postulacionDTO.getResumenIa());
            }
            postRepo.save(postulacionInicial);
        }
    }

    private PostulacionResponseDTO mapToResponse(Postulacion postulacion) {
        return PostulacionResponseDTO.builder()
                .id(postulacion.getId())
                .candidatoId(postulacion.getCandidato() != null ? postulacion.getCandidato().getId() : null)
                .ofertaId(postulacion.getOferta() != null ? postulacion.getOferta().getId() : null)
                .porcentajeMatch(postulacion.getPorcentajeMatch())
                .resumenIa(postulacion.getResumenIa())
                .build();
    }
}
