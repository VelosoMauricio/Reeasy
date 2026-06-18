package com.logistic.reeasy.demo.scan.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.logistic.reeasy.demo.scan.iface.ImageAnalyzerService;
import com.logistic.reeasy.demo.scan.models.ScanBottleDetail;
import com.logistic.reeasy.demo.scan.models.ScanResultWrapper;
import com.logistic.reeasy.demo.scan.models.AnalyzedResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class YoloImageAnalyzerService implements ImageAnalyzerService {

    private final RestTemplate restTemplate;
    private final String yoloApiUrl;

    public YoloImageAnalyzerService(@Value("${spring.yolo.api.url}") String yoloApiUrl) {
        this.restTemplate = new RestTemplate();
        this.yoloApiUrl = yoloApiUrl;
    }

    @Override
    public AnalyzedResult scanImage(String base64Image) {
        try {
            log.info("Starting image analysis using YOLO Service");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Constructing simple JSON for Python API
            Map<String, String> bodyMap = Map.of("image", base64Image);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(bodyMap, headers);

            // Direct call to /api/scan endpoint mapped to ScanResultWrapper
            ScanResultWrapper response = restTemplate.postForObject(yoloApiUrl + "/api/scan", request, ScanResultWrapper.class);

            if (response == null || response.getDetails() == null) {
                log.warn("YOLO service returned empty details");
                return new AnalyzedResult(base64Image, List.of());
            }

            // Aggregate counts by bottle type
            java.util.Map<com.logistic.reeasy.demo.scan.models.BottleType, Integer> counts = new java.util.HashMap<>();
            for (com.logistic.reeasy.demo.scan.models.YoloDetection det : response.getDetails()) {
                try {
                    com.logistic.reeasy.demo.scan.models.BottleType type = com.logistic.reeasy.demo.scan.models.BottleType.valueOf(det.getType().toUpperCase());
                    counts.put(type, counts.getOrDefault(type, 0) + 1);
                } catch (IllegalArgumentException e) {
                    log.warn("Unknown bottle type: {}", det.getType());
                }
            }

            List<ScanBottleDetail> detailsList = counts.entrySet().stream()
                    .map(entry -> new ScanBottleDetail(entry.getValue(), entry.getKey()))
                    .toList();

            log.info("Image analysis completed successfully");
            String resultImage = response.getImage() != null ? response.getImage() : base64Image;
            return new AnalyzedResult(resultImage, detailsList);

        } catch (HttpClientErrorException e) {
            String errorBody = e.getResponseBodyAsString();
            log.error("Error from YOLO service: {}", errorBody);
            throw new RuntimeException("Error from YOLO service: " + errorBody, e);
        } catch (Exception e) {
            log.error("Unexpected error during image analysis", e);
            throw new RuntimeException("Unexpected error during image analysis", e);
        }
    }
}
