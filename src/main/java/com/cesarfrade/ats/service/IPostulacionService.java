package com.cesarfrade.ats.service;

import com.cesarfrade.ats.dto.PostulacionRequestDTO;
import com.cesarfrade.ats.dto.PostulacionResponseDTO;
import com.cesarfrade.ats.model.Postulacion;

import java.util.List;

public interface IPostulacionService {
    public PostulacionResponseDTO findPostulacion(Long id_Postulacion);
    public List<PostulacionResponseDTO> getPostulaciones();
    public void savePostulacion(PostulacionRequestDTO postulacion);
    public void deletePostulacion(Long id_postulacion);
    public void editPostulacion(PostulacionRequestDTO postulacion, Long id_postulacion);
    List<PostulacionResponseDTO> getPostulacionesParaEmpresa(Long ofertaId, Double minMatch);
    public List<PostulacionResponseDTO> obtenerPostulacionesPorCandidato(Long candidatoId);
}
