package com.cesarfrade.ats.service;

import com.cesarfrade.ats.exception.NotFoundException;
import com.cesarfrade.ats.model.Postulacion;
import com.cesarfrade.ats.repository.PostulacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostulacionService implements IPostulacionService {
    private final PostulacionRepository postRepo;

    //Métodos CRUD
    @Override
    public Postulacion findPostulacion(Long id_Postulacion) {
        Postulacion postulacion = postRepo.findById(id_Postulacion).orElse(null);
        if (postulacion != null) {
            return postulacion;
        } else {
            throw new NotFoundException("Por el momento, no existe ninguna postulación"
                    + "con el id indicado");
        }
    }

    @Override
    public List<Postulacion> getPostulaciones() {
        return postRepo.findAll();
    }

    @Override
    public void savePostulacion(Postulacion postulacion) {
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
    public void editPostulacion(Postulacion postulacion, Long id_postulacion) {
        Postulacion postulacionInicial = postRepo.findById(id_postulacion).orElse(null);
        if (postulacionInicial == null) {
            throw new NotFoundException("Por el momento, no existe ninguna postulación"
                    + "con el id indicado");
        } else {
            if (postulacion.getCandidato() != null) {
                postulacionInicial.setCandidato(postulacion.getCandidato());
            }
            if (postulacion.getOferta() != null) {
                postulacionInicial.setOferta(postulacion.getOferta());
            }
            if (postulacion.getPorcentajeMatch() != null) {
                postulacionInicial.setPorcentajeMatch(postulacion.getPorcentajeMatch());
            }
            if (postulacion.getResumenIa() != null) {
                postulacionInicial.setResumenIa(postulacion.getResumenIa());
            }
            postRepo.save(postulacionInicial);
        }
    }
}
