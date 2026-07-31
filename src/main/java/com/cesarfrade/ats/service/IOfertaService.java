package com.cesarfrade.ats.service;

import com.cesarfrade.ats.model.Oferta;

import java.util.List;

public interface IOfertaService{
    public Oferta findOferta(Long id_oferta);
    public List<Oferta> getOfertas();
    public void saveOferta(Oferta oferta);
    public void deleteOferta(Long id_oferta);
    public void editOferta(Oferta oferta, Long id_oferta);
}
