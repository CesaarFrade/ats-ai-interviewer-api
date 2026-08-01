package com.cesarfrade.ats.service;

import com.cesarfrade.ats.model.Postulacion;

import java.util.List;

public interface IPostulacionService {
    public Postulacion findPostulacion(Long id_Postulacion);
    public List<Postulacion> getPostulaciones();
    public void savePostulacion(Postulacion cv);
    public void deletePostulacion(Long id_postulacion);
    public void editPostulacion(Postulacion postulacion, Long id_postulacion);
}
