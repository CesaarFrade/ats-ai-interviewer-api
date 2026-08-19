package com.cesarfrade.ats.service;

import com.cesarfrade.ats.dto.PostulacionRequestDTO;
import com.cesarfrade.ats.dto.PostulacionResponseDTO;
import com.cesarfrade.ats.exception.NotFoundException;
import com.cesarfrade.ats.model.CV;
import com.cesarfrade.ats.model.Candidato;
import com.cesarfrade.ats.model.Oferta;
import com.cesarfrade.ats.model.Postulacion;
import com.cesarfrade.ats.repository.CVRepository;
import com.cesarfrade.ats.repository.CandidatoRepository;
import com.cesarfrade.ats.repository.OfertaRepository;
import com.cesarfrade.ats.repository.PostulacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostulacionService implements IPostulacionService {
    private final PostulacionRepository postRepo;
    private final CandidatoRepository candRepo;
    private final OfertaRepository ofeRepo;
    private final CVRepository cvRepo;
    private final GeminiAIService aiService;

    // Métodos CRUD
    @Override
    public PostulacionResponseDTO findPostulacion(Long id_Postulacion) {
        Postulacion postulacion = postRepo.findById(id_Postulacion).orElse(null);
        if (postulacion != null) {
            return mapToResponse(postulacion);
        } else {
            throw new NotFoundException("Por el momento, no existe ninguna postulación"
                    + "con el id indicado");
        }
    }

    @Override
    public List<PostulacionResponseDTO> getPostulaciones() {
        return postRepo.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void savePostulacion(PostulacionRequestDTO postulacionDTO) {

        // Buscamos los datos base
        Candidato candidato = candRepo.findById(postulacionDTO.getCandidatoId()).orElse(null);
        if (candidato == null) {
            throw new NotFoundException("No existe ningún candidato con el id indicado");
        }

        Oferta oferta = ofeRepo.findById(postulacionDTO.getOfertaId()).orElse(null);
        if (oferta == null) {
            throw new NotFoundException("No existe ninguna oferta con el id indicado");
        }

        // Buscamos el CV del candidato
        List<CV> listaCvs = cvRepo.findByCandidatoId(candidato.getId());
        if (listaCvs.isEmpty()) {
            throw new NotFoundException("El candidato no tiene ningún CV subido para evaluar.");
        }
        CV cvCandidato = listaCvs.get(0);

        // Llamada a la inteligencia artificial
        String respuestaIA = aiService.evaluarCandidato(oferta.getDescripcionPuesto(), cvCandidato.getTexto_crudo());

        // Traducción respuesta
        Double matchCalculado = 0.0;
        String resumenGenerado = "Error al leer IA";

        try {
            // Separamos la respuesta de la IA por líneas
            String[] lineas = respuestaIA.split("\n");
            for (String linea : lineas) {
                if (linea.toUpperCase().startsWith("MATCH:")) {
                    // Quitamos la palabra "MATCH:" y nos quedamos solo con el número
                    matchCalculado = Double.parseDouble(linea.replace("MATCH:", "").replace("%", "").trim());
                }
                if (linea.toUpperCase().startsWith("RESUMEN:")) {
                    // Nos quedamos con el texto del resumen
                    resumenGenerado = linea.substring(8).trim();
                }
            }
        } catch (Exception e) {
            System.err.println("La IA respondió con un formato inesperado: " + respuestaIA);
            resumenGenerado = respuestaIA;
        }

        // Guardamos todo en la base de datos
        Postulacion postulacion = Postulacion.builder()
                .candidato(candidato)
                .oferta(oferta)
                .porcentajeMatch(matchCalculado)
                .resumenIa(resumenGenerado)
                .build();

        postRepo.save(postulacion);
    }

    @Override
    public void deletePostulacion(Long id_postulacion) {
        if (postRepo.existsById(id_postulacion)) {
            postRepo.deleteById(id_postulacion);
        } else {
            throw new NotFoundException("Por el momento, no existe ninguna postulación"
                    + "con el id indicado");
        }
    }

    @Override
    public void editPostulacion(PostulacionRequestDTO postulacionDTO, Long id_postulacion) {
        Postulacion postulacionInicial = postRepo.findById(id_postulacion).orElse(null);
        if (postulacionInicial == null) {
            throw new NotFoundException("Por el momento, no existe ninguna postulación"
                    + "con el id indicado");
        } else {
            if (postulacionDTO.getCandidatoId() != null) {
                Candidato candidato = candRepo.findById(postulacionDTO.getCandidatoId()).orElse(null);
                if (candidato == null) {
                    throw new NotFoundException("Por el momento, no existe ningún candidato"
                            + "con el id indicado");
                }
                postulacionInicial.setCandidato(candidato);
            }
            if (postulacionDTO.getOfertaId() != null) {
                Oferta oferta = ofeRepo.findById(postulacionDTO.getOfertaId()).orElse(null);
                if (oferta == null) {
                    throw new NotFoundException("Por el momento, no existe ninguna oferta"
                            + "con el id indicado");
                }
                postulacionInicial.setOferta(oferta);
            }
            postRepo.save(postulacionInicial);
        }
    }

    @Override
    public List<PostulacionResponseDTO> getPostulacionesParaEmpresa(Long ofertaId, Double minMatch) {
        List<Postulacion> filtradas = postRepo.findByOfertaIdAndPorcentajeMatchGreaterThanEqual(ofertaId, minMatch);

        return filtradas.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PostulacionResponseDTO mapToResponse(Postulacion postulacion) {
        return PostulacionResponseDTO.builder()
                .id(postulacion.getId())
                .candidatoId(postulacion.getCandidato() != null ? postulacion.getCandidato().getId() : null)
                .ofertaId(postulacion.getOferta() != null ? postulacion.getOferta().getId() : null)
                .porcentajeMatch(postulacion.getPorcentajeMatch())
                .resumenIa(postulacion.getResumenIa())
                .build();
    }

    // Método para obtener todas las postulaciones de un candidato específico
    public List<PostulacionResponseDTO> obtenerPostulacionesPorCandidato(Long candidatoId) {
        // Aseguramos que el candidato exista
        if (!candRepo.existsById(candidatoId)) {
            throw new NotFoundException("No existe ningún candidato con el id indicado");
        }

        // Buscamos las postulaciones filtrando por el ID del candidato y las mapeamos a DTO
        return postRepo.findByCandidatoId(candidatoId).stream()
                .map(this::mapToResponse)
                .toList();
    }
}
