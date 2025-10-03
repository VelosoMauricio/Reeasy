package com.logistic.reeasy.demo.scan.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.logistic.reeasy.demo.common.exception.custom.PlasticBottleNotDetected;
import com.logistic.reeasy.demo.scan.dto.ScanBottleDetailDto;
import com.logistic.reeasy.demo.scan.dto.ScanDto;
import com.logistic.reeasy.demo.scan.iface.ScanDAO;
import com.logistic.reeasy.demo.scan.models.ScanBottleDetail;
import com.logistic.reeasy.demo.scan.models.ScanModel;

@Service
public class RecyclingService {

  private final ImageAnalyzerService imageAnalyzerService;
  private final ScanDAO scanDAOImpl;

  public RecyclingService(
      ImageAnalyzerService imageAnalyzerService,
      ScanDAO scanDAOImpl) {
        
    this.imageAnalyzerService = imageAnalyzerService;
    this.scanDAOImpl = scanDAOImpl;
  }

  public ScanDto scanImage(String image, Long id) {

    List<ScanBottleDetail> detailsBottlesList = imageAnalyzerService.scanImage(image);

    if(detailsBottlesList == null || detailsBottlesList.isEmpty()) {
      throw new PlasticBottleNotDetected("The image does not contain recyclable plastic bottles");
    }

    String pureBase64 = image;
    
    if (pureBase64.contains(",")) {
        pureBase64 = pureBase64.split(",")[1];
    }

    byte[] imageBytes = pureBase64.getBytes();

    ScanModel scanModel = new ScanModel(
        1L,
        null,
        imageBytes,
        detailsBottlesList
    );

    try{
      scanModel = scanDAOImpl.save(scanModel);

      List<ScanBottleDetailDto> bottleDetails = scanModel
        .getData().stream()
        .map(detail -> new ScanBottleDetailDto(detail.getType(), detail.getAmount()))
        .toList();

      return new ScanDto(scanModel.getDate(), bottleDetails);
    }
    catch(Exception e){

      // TODO: Manejar errores SQL

      throw new RuntimeException("It happened an error on save scan", e);
    }
  }
}