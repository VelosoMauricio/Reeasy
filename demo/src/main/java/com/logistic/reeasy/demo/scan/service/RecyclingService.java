package com.logistic.reeasy.demo.scan.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.logistic.reeasy.demo.scan.models.BottleType;
import com.logistic.reeasy.demo.scan.models.ScanBottleDetail;
import com.logistic.reeasy.demo.scan.models.ScanModel;

@Service
public class RecyclingService {

  private final ImageAnalyzerService imageAnalyzerService;

  public RecyclingService(
      ImageAnalyzerService imageAnalyzerService) {
    this.imageAnalyzerService = imageAnalyzerService;
  }

  public ScanModel scanImage(String image, Long id) {

    // llamada al servicio de analisis de imagen

    imageAnalyzerService.scanImage(image, id);

    ScanModel example = new ScanModel(
        LocalDate.now(),
        image,
        id,
        List.of(
            new ScanBottleDetail(5, BottleType.PET1),
            new ScanBottleDetail(3, BottleType.HDPE)) // le pasamos la lista de botellas o llamamos varias veces al
                                                      // scan?
    );

    return example;
  }
}