package com.logistic.reeasy.demo.scan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.logistic.reeasy.demo.scan.iface.ImageAnalyzerService;
import com.logistic.reeasy.demo.scan.models.ScanBottleDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Primary
public class FailoverImageAnalyzerService implements ImageAnalyzerService{

    private final ImageAnalyzerService servicioPrincipal;
    private final ImageAnalyzerService servicioFallback;
    // private final CircuitBreaker circuitBreaker; // se viene lo chido

    public FailoverImageAnalyzerService(
        @Qualifier("LocalAnalizer") ImageAnalyzerService servicioPrincipal,
        @Qualifier("GeminiAnalyzer") ImageAnalyzerService servicioFallback
    ) {
        this.servicioPrincipal = servicioPrincipal;
        this.servicioFallback = servicioFallback;
        // Aquí inicializarías tu Circuit Breaker
    }

    @Override
    public List<ScanBottleDetail> scanImage(String image) {
        // Lógica simple de fallback (idealmente usarías un Circuit Breaker)
        try {
            log.debug("Intentando análisis con servicio principal...");
            return servicioFallback.scanImage(image);
        } catch (Exception e) {
            log.warn("Servicio principal falló: {}. Usando fallback (Gemini).", e.getMessage());
            return servicioPrincipal.scanImage(image);
        }
    }
}
