package com.cesarfrade.ats.service;

import com.cesarfrade.ats.model.Entrevista;

public interface IEntrevistaService {
    public Entrevista iniciarEntrevista(Long postulacionId);
    public String enviarMensaje(Long entrevistaId, String mensajeCandidato);

}
