package com.logistic.reeasy.demo.scan.service;

import java.util.List;
import java.util.Map;

import com.logistic.reeasy.demo.common.exception.custom.GoogleApiServiceException;
import com.logistic.reeasy.demo.common.exception.custom.InvalidApiKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.logistic.reeasy.demo.scan.models.ScanBottleDetail;
import com.logistic.reeasy.demo.scan.models.ScanResultWrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class ImageAnalyzerService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String GEMINI_URL;
    private final String GEMINI_API_KEY;

    public ImageAnalyzerService(
            ObjectMapper objectMapper,
            @Value("${gemini.api.url}") String geminiUrl,
            @Value("${gemini.api.key}") String geminiApiKey
    ) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;

        this.GEMINI_URL = geminiUrl;
        this.GEMINI_API_KEY = geminiApiKey;
    }

    public List<ScanBottleDetail> scanImage(String base64Image) {
        try {
            String prompt = buildPrompt();

            String body = buildRequestBody(prompt, base64Image);

            log.info("Starting image analysis using Gemini API");
            // Llamada a la API
            JsonNode response = callGeminiApi(body);

            // Extraer el JSON de la respuesta
            String jsonResponse = extractJsonFromResponse(response);

            ScanResultWrapper wrapper = mapResponseToScanResult(jsonResponse);

            log.info("Image analysis completed successfully");

            return wrapper.getDetails();

        } catch (HttpClientErrorException.BadRequest e) {
            handleBadRequest(e);
            return null; // Nunca llega aca porque handleBadRequest lanza excepcion
        } catch (Exception e) {
            e.printStackTrace();
            throw new GoogleApiServiceException("It happend an inespered error", e);
        }
    }

    private ScanResultWrapper mapResponseToScanResult(String jsonResponse) throws Exception {
        return objectMapper.readValue(jsonResponse, ScanResultWrapper.class);
    }

    private String extractJsonFromResponse(JsonNode response) {
        // Chusmeamos la respuesta para sacar el JSON. Basicamente navegamos dentro de
        // la estructura que esta mas abajo y sacamos el texto
        JsonNode candidateNode = response.at("/candidates/0/content/parts/0/text");

        // Nos fijamos que haya algo
        if (candidateNode.isMissingNode() || candidateNode.asText().isEmpty()) {
            throw new RuntimeException("Inespered response: " + response.toString());
        }

        // Limpiamos el texto para quedarnos solo con el JSON. Limpia los '''' fences
        // del MARKDOWN
        return candidateNode.asText()
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
    }

    private JsonNode callGeminiApi(String body) {
        HttpHeaders headers = new HttpHeaders();

        // Le aviso que le va a llegar un JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // Le hago un POST hard a la api de Gemini
        return restTemplate.postForObject(GEMINI_URL + "?key=" + GEMINI_API_KEY, request, JsonNode.class);
    }

    private String buildPrompt() {
        return """
                    Analiza la imagen y devuelve SOLO un JSON con este formato EXACTO:
                    {
                    "details": [
                        {"type": "PET", "amount": 0},
                        {"type": "PEAD", "amount": 0},
                        {"type": "PEBD", "amount": 0}
                    ]
                    }
                    La clave es que la IA debe reemplazar los ceros con la cantidad real de cada tipo de plástico que aparezca en la imagen.
                    No pongas texto extra, solo JSON.
                    Solo indica los tipos de plástico presentes, osea en caso de tener amount 0, no lo incluyas en el JSON.
                """;
    }

    // Armamos el JSON que espera la API de Gemini
    private String buildRequestBody(String prompt, String base64Image) throws Exception {

        // Construimos el bloque del prompt
        Map<String, Object> textPart = Map.of("text", prompt);

        // El bloque de la imagen en base64
        Map<String, Object> imagePart = Map.of(
                "inline_data", Map.of("mime_type", "image/jpeg", "data", base64Image));

        // El rol + el contenido
        Map<String, Object> userContent = Map.of("role", "user", "parts", List.of(textPart, imagePart));

        // Metemos todo en un array content (lo que espera la api de gemini)
        Map<String, Object> bodyMap = Map.of("contents", List.of(userContent));

        return objectMapper.writeValueAsString(bodyMap); // Lo pasamos a JSON
    }

    private void handleBadRequest(HttpClientErrorException.BadRequest e) {
        String errorBody = e.getResponseBodyAsString();
        if (errorBody != null && errorBody.contains("API_KEY_INVALID")) {
            log.error("Invalid API Key provided for Gemini API");
            throw new InvalidApiKeyException("INVALID API KEY");
        } else {
            log.error("Bad request to Gemini API: {}", errorBody);
            throw new GoogleApiServiceException("Gemini API returned bad request", e);
        }
    }

}