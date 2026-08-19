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
    private final GeminiAIService geminiAIService;

    public Entrevista iniciarEntrevista(Long postulacionId) {
        Postulacion postulacion = postulacionRepository.findById(postulacionId)
                .orElseThrow(() -> new NotFoundException("Postulación no encontrada"));

        return entrevistaRepository.findByPostulacionId(postulacionId).orElseGet(() -> {

            // PROMPT INICIAL ESTRICTO
            String promptInicial = String.format(
                    "Eres un Tech Lead experto de recursos humanos. Vas a realizar una entrevista técnica corta (de 3 o 4 preguntas) a un candidato.\n" +
                            "Puesto de trabajo: %s\n" +
                            "Descripción del puesto: %s\n\n" +
                            "REGLAS ESTRICTAS DE FORMATO:\n" +
                            "1. Saluda al candidato de forma profesional, haz una breve introducción al puesto y lánzale la primera pregunta técnica basada en la oferta.\n" +
                            "2. Háblale DIRECTAMENTE al candidato como si estuvierais en un chat.\n" +
                            "3. PROHIBIDO escribir 'MATCH:' o 'RESUMEN:' en tu respuesta. Nunca incluyas notas internas.\n" +
                            "4. Regla de finalización: Solo debes realizar un máximo de 3 o 4 preguntas técnicas en total. Cuando creas que ya tienes suficiente información para evaluar al candidato, despídete cordialmente, agradécele su tiempo y añade obligatoriamente la etiqueta [FIN_ENTREVISTA] al final de tu mensaje.",
                    postulacion.getOferta().getTitulo(),
                    postulacion.getOferta().getDescripcionPuesto()
            );

            String respuestaCrudaIa = geminiAIService.evaluarCandidato(promptInicial, "Inicio de entrevista técnica.");

            // LIMPIEZA EXTREMA DEL SALUDO INICIAL
            String respuestaLimpia = respuestaCrudaIa.replaceAll("(?i)MATCH:.*", "")
                    .replaceAll("(?is)RESUMEN:.*?(?=\n\n|$)", "")
                    .trim();

            Entrevista nuevaEntrevista = Entrevista.builder()
                    .postulacion(postulacion)
                    .historialConversacion("IA: " + respuestaLimpia + "\n")
                    .estado("EN_PROCESO")
                    .fechaInicio(LocalDateTime.now())
                    .build();

            return entrevistaRepository.save(nuevaEntrevista);
        });
    }

    public String enviarMensaje(Long entrevistaId, String mensajeCandidato) {
        Entrevista entrevista = entrevistaRepository.findById(entrevistaId)
                .orElseThrow(() -> new NotFoundException("Entrevista no encontrada"));

        String historialActualizado = entrevista.getHistorialConversacion() + "Candidato: " + mensajeCandidato + "\n";

        // PROMPT CONTINUACIÓN ESTRICTO
        String promptContinuacion = String.format(
                "Eres un Tech Lead realizando una entrevista técnica. Este es el historial de la conversación:\n%s\n" +
                        "Evalúa la respuesta del candidato internamente y hazle la siguiente pregunta.\n\n" +
                        "REGLAS ESTRICTAS DE FORMATO:\n" +
                        "1. Háblale DIRECTAMENTE al candidato evaluando su respuesta de forma constructiva.\n" +
                        "2. PROHIBIDO escribir 'MATCH:' o 'RESUMEN:' en tu respuesta. Nunca incluyas notas internas, solo el diálogo.\n" +
                        "3. Haz un máximo de 3 preguntas en total. Cuando termines de evaluar (o si ya has hecho 3 preguntas), despídete cordialmente y añade OBLIGATORIAMENTE la etiqueta [FIN_ENTREVISTA] al final del mensaje.",
                historialActualizado
        );

        String respuestaCrudaIa = geminiAIService.evaluarCandidato("Continuación de entrevista", promptContinuacion);

        // --- 1. LIMPIEZA EXTREMA CON REGEX (Anti alucinaciones de la IA) ---
        String respuestaLimpia = respuestaCrudaIa.replaceAll("(?i)MATCH:.*", "")
                .replaceAll("(?is)RESUMEN:.*?(?=\n\n|$)", "")
                .trim();

        // Si la IA solo generó notas internas y las borramos todas, no dejamos el chat roto
        if (respuestaLimpia.isEmpty()) {
            respuestaLimpia = "Me parece una respuesta excelente. Con todo lo que hemos hablado tengo información más que suficiente para valorar tu perfil. ¡Muchísimas gracias por tu tiempo! [FIN_ENTREVISTA]";
        }

        // --- 2. DETECTAR EL FIN DE LA ENTREVISTA Y EVALUAR ---
        boolean entrevistaFinalizada = false;
        if (respuestaLimpia.contains("[FIN_ENTREVISTA]")) {
            entrevistaFinalizada = true;
            // Le quitamos la etiqueta oculta
            respuestaLimpia = respuestaLimpia.replace("[FIN_ENTREVISTA]", "").trim();
            // Actualizamos el estado de la entrevista en la BD
            entrevista.setEstado("FINALIZADA");

            // Construimos el historial con la última respuesta limpia incluida
            String historialCompleto = historialActualizado + "IA: " + respuestaLimpia;

            String promptEvaluacionFinal = String.format(
                    "Lee la siguiente entrevista técnica completa entre un candidato y un Tech Lead:\n%s\n\n" +
                            "Actúa como un evaluador técnico. Analiza las respuestas del candidato y genera una evaluación final. " +
                            "Tu respuesta DEBE tener EXACTAMENTE este formato y nada más:\n" +
                            "MATCH: <número del 0 al 100>\n" +
                            "RESUMEN: <resumen de 3 o 4 líneas sobre sus habilidades técnicas y comunicación>",
                    historialCompleto
            );

            try {
                String evaluacionSilenciosa = geminiAIService.evaluarCandidato("Evaluación post-entrevista", promptEvaluacionFinal);

                // Usamos Expresiones Regulares para extraer el número y el texto
                String matchStr = evaluacionSilenciosa.replaceAll("(?is).*MATCH:\\s*(\\d+).*", "$1").trim();
                String resumenStr = evaluacionSilenciosa.replaceAll("(?is).*RESUMEN:\\s*(.*)", "$1").trim();

                // Sobrescribimos la Postulación con los datos definitivos tras la entrevista
                Postulacion postulacion = entrevista.getPostulacion();
                postulacion.setPorcentajeMatch(Double.parseDouble(matchStr));
                postulacion.setResumenIa(resumenStr);

                postulacionRepository.save(postulacion);
            } catch (Exception e) {
                System.err.println("Error al extraer la evaluación final de Gemini: " + e.getMessage());
            }
        }

        // Guardamos el historial LIMPIO en la base de datos
        historialActualizado = entrevista.getHistorialConversacion() + "Candidato: " + mensajeCandidato + "\nIA: " + respuestaLimpia + "\n";
        entrevista.setHistorialConversacion(historialActualizado);

        entrevistaRepository.save(entrevista);

        // --- 3. RESPONDER AL FRONTEND ---
        if (entrevistaFinalizada) {
            return "FINALIZADA|" + respuestaLimpia;
        }

        return respuestaLimpia;
    }

    public Optional<Entrevista> obtenerPorPostulacion(Long postulacionId) {
        return entrevistaRepository.findByPostulacionId(postulacionId);
    }
}
