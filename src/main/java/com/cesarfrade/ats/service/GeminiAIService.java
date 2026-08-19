package com.cesarfrade.ats.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiAIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    // Herramientas de Spring Boot para hacer peticiones HTTP y leer JSON
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String evaluarCandidato(String descripcionOferta, String textoCv) {

        // 1. Diseñamos el Prompt
        String prompt = "Eres un Tech Lead evaluando a un candidato. Lee esta oferta y este CV.\n" +
                "OFERTA: " + descripcionOferta + "\n" +
                "CV: " + textoCv + "\n\n" +
                "Devuelve una respuesta estructurada así:\n" +
                "MATCH: [Número del 0 al 100]\n" +
                "RESUMEN: [Tu opinión en 2 o 3 líneas]";

        // 2. JSON exacto que exige Gemini
        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> requestBody = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Añadimos la clave de seguridad a la URL
        String urlConToken = apiUrl + "?key=" + apiKey;
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            // 3. Disparamos la petición hacia los servidores de Google
            String response = restTemplate.postForObject(urlConToken, request, String.class);

            // 4. Navegamos por el JSON de respuesta para sacar solo el texto que nos interesa
            JsonNode rootNode = objectMapper.readTree(response);
            return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

        } catch (Exception e) {
            System.err.println("Error al comunicarse con Gemini: " + e.getMessage());
            return "MATCH: 0\nRESUMEN: Error al evaluar con la IA.";
        }
    }

    // Método auxiliar para texto libre en entrevistas
    public String generarTextoLibre(String prompt) {
        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> requestBody = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String urlConToken = apiUrl + "?key=" + apiKey;
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            String response = restTemplate.postForObject(urlConToken, request, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
        } catch (Exception e) {
            System.err.println("Error al comunicarse con Gemini: " + e.getMessage());
            return "Hola, ha ocurrido un error de conexión con la IA.";
        }
    }
}
