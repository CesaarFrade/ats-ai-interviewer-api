package com.cesarfrade.ats.service;

import com.cesarfrade.ats.exception.NotFoundException;
import com.cesarfrade.ats.model.CV;
import com.cesarfrade.ats.repository.CVRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CVService implements ICVService {
    private final CVRepository cvRepo;

    //Métodos CRUD
    @Override
    public CV findCV(Long id_CV) {
        CV cv = cvRepo.findById(id_CV).orElse(null);
        if (cv != null) {
            return cv;
        } else {
            throw new NotFoundException("Por el momento, no existe ningún currículum"
                    + "con el id indicado");
        }
    }

    @Override
    public List<CV> getCVs() {
        return cvRepo.findAll();
    }

    @Override
    public void saveCV(CV cv) {
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
    public void editCV(CV cv, Long id_cv) {
        CV cvInicial = cvRepo.findById(id_cv).orElse(null);
        if (cvInicial == null) {
            throw new NotFoundException("Por el momento, no existe ningún currículum"
                    + "con el id indicado");
        } else {
            if (cv.getCandidato() != null) {
                cvInicial.setCandidato(cv.getCandidato());
            }
            if (cv.getRuta_archivo_S3() != null) {
                cvInicial.setRuta_archivo_S3(cv.getRuta_archivo_S3());
            }
            if (cv.getTexto_crudo() != null) {
                cvInicial.setTexto_crudo(cv.getTexto_crudo());
            }
            cvRepo.save(cvInicial);
        }
    }
}
