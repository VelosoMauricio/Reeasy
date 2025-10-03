package com.logistic.reeasy.demo.scan.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.logistic.reeasy.demo.scan.models.ScanBottleDetail;
import com.logistic.reeasy.demo.scan.models.ScanResultWrapper;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class ImageAnalyzerService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ImageAnalyzerService(ObjectMapper objectMapper) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    public List<ScanBottleDetail> scanImage(String base64Image, Long userId) {
        try {

            String prompt = """
                        Analiza la imagen y devuelve SOLO un JSON con este formato EXACTO:
                        {
                        "details": [
                            {"type": "PET1", "amount": 0},
                            {"type": "HDPE", "amount": 0},
                            {"type": "LDPE", "amount": 0}
                        ]
                        }
                        La clave es que la IA debe reemplazar los ceros con la cantidad real de cada tipo de plástico que aparezca en la imagen.
                        No pongas texto extra, solo JSON.
                        Solo indica los tipos de plástico presentes, osea en caso de tener amount 0, no lo incluyas en el JSON.
                    """;

            Map<String, Object> textPart = Map.of("text", prompt);

            Map<String, Object> imagePart = Map.of(
                    "inline_data", Map.of(
                            "mime_type", "image/jpeg",
                            "data", base64Image));

            Map<String, Object> userContent = Map.of(
                    "role", "user",
                    "parts", List.of(textPart, imagePart));

            Map<String, Object> bodyMap = Map.of(
                    "contents", List.of(userContent));

            // Convertimos a JSON String seguro
            String body = objectMapper.writeValueAsString(bodyMap);
            // The working URL based on your successful curl request
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyAoYk2ZefMwsr_kDx90NG3ISOdx55TKMSY";
            // Configurar headers para JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(body, headers);

            // Llamada a la API
            JsonNode response = restTemplate.postForObject(url, request, JsonNode.class);

            // Validar respuesta
            JsonNode candidateNode = response.at("/candidates/0/content/parts/0/text");
            if (candidateNode.isMissingNode() || candidateNode.asString().isEmpty()) {
                throw new RuntimeException("Respuesta inesperada de la API de Gemini: " + response.toString());
            }

            String jsonResponse = candidateNode.asString();

            // para limpiarkle los ticks al json... nashe

            jsonResponse = jsonResponse
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            // debug para ver si salio todo bien
            System.out.println("JSON limpio de Gemini: " + jsonResponse);

            // Parsear a ScanModel
            ScanResultWrapper wrapper = objectMapper.readValue(jsonResponse, ScanResultWrapper.class);

            // Extraer la lista de detalles del wrapper
            List<ScanBottleDetail> detailsList = wrapper.getDetails();

            return detailsList;
        } catch (Exception e) {
            throw new RuntimeException("Error al analizar la imagen", e);
        }
    }

}