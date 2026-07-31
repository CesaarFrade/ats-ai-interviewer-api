package com.cesarfrade.ats.service;

import com.cesarfrade.ats.exception.NotFoundException;
import com.cesarfrade.ats.model.Oferta;
import com.cesarfrade.ats.repository.OfertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfertaService implements IOfertaService{
    private final OfertaRepository ofeRepo;

    //Métodos CRUD
    @Override
    public Oferta findOferta(Long id_oferta) {
        Oferta oferta = ofeRepo.findById(id_oferta).orElse(null);
        if(oferta != null){
            return oferta;
        } else{
            throw new NotFoundException("Por el momento, no existe ninguna oferta"
                    + "con el id indicado");
        }
    }

    @Override
    public List<Oferta> getOfertas() {
        return ofeRepo.findAll();
    }

    @Override
    public void saveOferta(Oferta oferta) {
        ofeRepo.save(oferta);
    }

    @Override
    public void deleteOferta(Long id_oferta) {
        if(ofeRepo.existsById(id_oferta)){
            ofeRepo.deleteById(id_oferta);
        } else{
            throw new NotFoundException("Por el momento, no existe ninguna oferta"
                    + "con el id indicado");
        }
    }

    @Override
    public void editOferta(Oferta oferta, Long id_oferta) {
        Oferta ofertaInicial = ofeRepo.findById(id_oferta).orElse(null);
        if(ofertaInicial == null){
            throw new NotFoundException("Por el momento, no existe ninguna oferta"
                    + "con el id indicado");
        } else{
            ofertaInicial.setActiva(oferta.isActiva());
            if(oferta.getTitulo() != null){
                ofertaInicial.setTitulo(oferta.getTitulo());
            }
            if(oferta.getDescripcionPuesto() != null){
                ofertaInicial.setDescripcionPuesto(oferta.getDescripcionPuesto());
            }
            if(oferta.getFechaPublicacion() != null) {
                ofertaInicial.setFechaPublicacion(ofertaInicial.getFechaPublicacion());
            }
            ofeRepo.save(ofertaInicial);
        }
    }
}
