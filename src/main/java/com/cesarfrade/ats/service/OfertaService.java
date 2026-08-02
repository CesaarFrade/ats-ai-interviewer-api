package com.cesarfrade.ats.service;

import com.cesarfrade.ats.dto.OfertaRequestDTO;
import com.cesarfrade.ats.dto.OfertaResponseDTO;
import com.cesarfrade.ats.exception.NotFoundException;
import com.cesarfrade.ats.model.Oferta;
import com.cesarfrade.ats.repository.OfertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfertaService implements IOfertaService {
    private final OfertaRepository ofeRepo;

    //Métodos CRUD
    @Override
    public OfertaResponseDTO findOferta(Long id_oferta) {
        Oferta oferta = ofeRepo.findById(id_oferta).orElse(null);
        if (oferta != null) {
            return mapToResponse(oferta);
        } else {
            throw new NotFoundException("Por el momento, no existe ninguna oferta"
                    + "con el id indicado");
        }
    }

    @Override
    public List<OfertaResponseDTO> getOfertas() {
        return ofeRepo.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void saveOferta(OfertaRequestDTO ofertaDTO) {
        Oferta oferta = Oferta.builder()
                .titulo(ofertaDTO.getTitulo())
                .descripcionPuesto(ofertaDTO.getDescripcionPuesto())
                .activa(ofertaDTO.isActiva())
                .build();
        ofeRepo.save(oferta);
    }

    @Override
    public void deleteOferta(Long id_oferta) {
        if (ofeRepo.existsById(id_oferta)) {
            ofeRepo.deleteById(id_oferta);
        } else {
            throw new NotFoundException("Por el momento, no existe ninguna oferta"
                    + "con el id indicado");
        }
    }

    @Override
    public void editOferta(OfertaRequestDTO ofertaDTO, Long id_oferta) {
        Oferta ofertaInicial = ofeRepo.findById(id_oferta).orElse(null);
        if (ofertaInicial == null) {
            throw new NotFoundException("Por el momento, no existe ninguna oferta"
                    + "con el id indicado");
        } else {
            ofertaInicial.setActiva(ofertaDTO.isActiva());
            if (ofertaDTO.getTitulo() != null) {
                ofertaInicial.setTitulo(ofertaDTO.getTitulo());
            }
            if (ofertaDTO.getDescripcionPuesto() != null) {
                ofertaInicial.setDescripcionPuesto(ofertaDTO.getDescripcionPuesto());
            }
            ofeRepo.save(ofertaInicial);
        }
    }

    private OfertaResponseDTO mapToResponse(Oferta oferta) {
        return OfertaResponseDTO.builder()
                .id(oferta.getId())
                .titulo(oferta.getTitulo())
                .descripcionPuesto(oferta.getDescripcionPuesto())
                .fechaPublicacion(oferta.getFechaPublicacion())
                .activa(oferta.isActiva())
                .build();
    }
}
