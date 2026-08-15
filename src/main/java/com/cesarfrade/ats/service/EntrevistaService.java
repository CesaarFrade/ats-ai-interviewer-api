package com.cesarfrade.ats.service;

import com.cesarfrade.ats.exception.NotFoundException;
import com.cesarfrade.ats.model.Entrevista;
import com.cesarfrade.ats.model.Postulacion;
import com.cesarfrade.ats.repository.EntrevistaRepository;
import com.cesarfrade.ats.repository.PostulacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntrevistaService {

    private final EntrevistaRepository entrevistaRepository;
    private final PostulacionRepository postulacionRepository;
    private final GeminiAIService geminiAIService; // Usamos tu servicio HTTP de Gemini

    // 1. Iniciar la entrevista para una postulación
    public Entrevista iniciarEntrevista(Long postulacionId) {
        Postulacion postulacion = postulacionRepository.findById(postulacionId)
                .orElseThrow(() -> new NotFoundException("Postulación no encontrada"));

        return entrevistaRepository.findByPostulacionId(postulacionId).orElseGet(() -> {

            // Construimos el prompt inicial
            String promptInicial = String.format(
                    "Eres un entrevistador técnico experto de recursos humanos. Vas a realizar una entrevista técnica corta (de 3 o 4 preguntas) a un candidato.\n" +
                            "Puesto de trabajo: %s\n" +
                            "Descripción del puesto: %s\n\n" +
                            "Por favor, saluda al candidato de forma profesional, haz una breve introducción al puesto y lánzale la primera pregunta técnica basada en la oferta.",
                    postulacion.getOferta().getTitulo(),
                    postulacion.getOferta().getDescripcionPuesto()
            );

            // Llamamos a tu GeminiAIService reutilizando el método de evaluación o adaptándolo
            String respuestaIa = geminiAIService.evaluarCandidato(promptInicial, "Inicio de entrevista técnica.");

            Entrevista nuevaEntrevista = Entrevista.builder()
                    .postulacion(postulacion)
                    .historialConversacion("IA: " + respuestaIa + "\n")
                    .estado("EN_PROCESO")
                    .fechaInicio(LocalDateTime.now())
                    .build();

            return entrevistaRepository.save(nuevaEntrevista);
        });
    }

    // 2. Enviar respuesta del candidato y obtener la réplica de la IA
    public String enviarMensaje(Long entrevistaId, String mensajeCandidato) {
        Entrevista entrevista = entrevistaRepository.findById(entrevistaId)
                .orElseThrow(() -> new NotFoundException("Entrevista no encontrada"));

        String historialActualizado = entrevista.getHistorialConversacion() + "Candidato: " + mensajeCandidato + "\n";

        String promptContinuacion = String.format(
                "Estás realizando una entrevista técnica. Este es el historial de la conversación:\n%s\n" +
                        "El candidato acaba de responder lo anterior. Evalúa su respuesta, sé constructivo y haz la siguiente pregunta técnica, o si ya llevamos suficientes preguntas, despide amablemente al candidato.",
                historialActualizado
        );

        String respuestaIa = geminiAIService.evaluarCandidato("Continuación de entrevista", promptContinuacion);

        historialActualizado += "IA: " + respuestaIa + "\n";
        entrevista.setHistorialConversacion(historialActualizado);

        entrevistaRepository.save(entrevista);

        return respuestaIa;
    }

    public Optional<Entrevista> obtenerPorPostulacion(Long postulacionId) {
        return entrevistaRepository.findByPostulacionId(postulacionId);
    }
}