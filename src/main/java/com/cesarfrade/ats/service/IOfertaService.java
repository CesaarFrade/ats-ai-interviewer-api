package com.cesarfrade.ats.service;

import com.cesarfrade.ats.dto.OfertaRequestDTO;
import com.cesarfrade.ats.dto.OfertaResponseDTO;

import java.util.List;

public interface IOfertaService {
    public OfertaResponseDTO findOferta(Long id_oferta);
    public List<OfertaResponseDTO> getOfertas();
    public List<OfertaResponseDTO> getOfertasByEmpresa(String emailCreador);
    public void saveOferta(OfertaRequestDTO oferta, String emailCreador);
    public void deleteOferta(Long id_oferta);
    public void editOferta(OfertaRequestDTO oferta, Long id_oferta);
}
