package com.logistic.reeasy.demo.scan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name = "iaPrincipal", fallbackMethod = "analisisFallback")
    public List<ScanBottleDetail> scanImage(String image) {
        log.debug("Realizando análisis con servicio principal...");
        return servicioPrincipal.scanImage(image);
    }

    private List<ScanBottleDetail> analisisFallback(String image, Throwable t) {
        log.warn("Circuit Breaker 'iaPrincipal' activado. Usando fallback (Gemini). Error: {}", t.getMessage());
        // Llama a tu servicio de Gemini
        return servicioFallback.scanImage(image);
    }
}
