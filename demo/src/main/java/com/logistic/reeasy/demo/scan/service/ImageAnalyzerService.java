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

import com.logistic.reeasy.demo.scan.models.RequestModel;
import com.logistic.reeasy.demo.scan.models.ScanBottleDetail;
import com.logistic.reeasy.demo.scan.models.ScanResultWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class ImageAnalyzerService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    //private final String GEMINI_URL;
    //private final String GEMINI_API_KEY;
    private final String CUSTOM_API_URL; //nueva url las anteriores quedan en desuso

    public ImageAnalyzerService(
            ObjectMapper objectMapper,
            //@Value("${gemini.api.url}") String geminiUrl,
            //@Value("${gemini.api.key}") String geminiApiKey
            @Value("${gemini.api.custom}") String customUrl
    ) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;

        //this.GEMINI_URL = geminiUrl;
        //this.GEMINI_API_KEY = geminiApiKey;
        this.CUSTOM_API_URL = customUrl;
    }

    public List<ScanBottleDetail> scanImage(String base64Image) {
        try {
            String prompt = buildPrompt();

            String body = buildRequestBody(prompt, base64Image);

            // Llamada a la API Gemini
            //JsonNode response = callGeminiApi(body);
            
            // Lamada a la API propia
            JsonNode response = callApi(body);
            /* 
            log.info("Starting image analysis using Gemini API");
            // Llamada a la API
            JsonNode response = callGeminiApi(body);
            */
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
        //JsonNode candidateNode = response.at("/candidates/0/content/parts/0/text"); en el nuevo apuntamos al candidato response
        JsonNode candidateNode = response.at("/response");

        // Nos fijamos que haya algo
        if (candidateNode.isMissingNode() || candidateNode.asText().isEmpty())
            throw new RuntimeException("Respuesta inesperada de la API custom: No se encontró el campo '/response' o está vacío. Respuesta recibida: " + response.toString());

        // Limpiamos el texto para quedarnos solo con el JSON. Limpia los '''' fences
        // del MARKDOWN
        return candidateNode.asText()
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
    }

    private JsonNode callApi(String body){
        
        try {
            HttpHeaders headers = new HttpHeaders();
            
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(body, headers);

            // Haces el POST
            return restTemplate.postForObject(CUSTOM_API_URL, request, JsonNode.class);

        } catch (HttpClientErrorException e) {
            // Esto es útil para debuggear si tu API local falla
            String errorBody = e.getResponseBodyAsString();
            System.err.println("Error llamando a la API custom: " + errorBody);
            throw new RuntimeException("Error en la API custom: " + errorBody, e);
        }
    }

    /* Anterior llamada a la api de google gemmini
    private JsonNode callGeminiApi(String body) {
        HttpHeaders headers = new HttpHeaders();

        // Le aviso que le va a llegar un JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // Le hago un POST hard a la api de Gemini
        return restTemplate.postForObject(GEMINI_URL + "?key=" + GEMINI_API_KEY, request, JsonNode.class);
    }*/

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


    /*
    /////Fragmento para armar el JSON que requiere la api de google, no la usamos ya que vamos a necesitar un JSON distinto para nuestr api
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
        
    */

    // Armamos el JSON para la api propia
    private String buildRequestBody(String prompt, String base64Image) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        RequestModel request = new RequestModel();
        request.setModel("gemma3:12b");
        request.setPrompt(prompt);
        request.setImages(base64Image);

        System.out.println(request);

        return mapper.writeValueAsString(request);
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