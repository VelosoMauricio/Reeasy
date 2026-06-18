package com.logistic.reeasy.demo.scan.service;

import java.sql.Timestamp;
import java.util.List;

import com.logistic.reeasy.demo.scan.models.ScanTableModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.logistic.reeasy.demo.common.exception.custom.PlasticBottleNotDetected;
import com.logistic.reeasy.demo.scan.dto.ScanBottleDetailDto;
import com.logistic.reeasy.demo.scan.dto.ScanDto;
import com.logistic.reeasy.demo.scan.iface.ImageAnalyzerService;
import com.logistic.reeasy.demo.scan.iface.ScanDAO;
import com.logistic.reeasy.demo.scan.models.ScanBottleDetail;

@Slf4j
@Service
public class RecyclingService {
    private final ImageAnalyzerService imageAnalyzerService;
    private final ScanDAO scanDAOImpl;

    public RecyclingService(ImageAnalyzerService imageAnalyzerService, ScanDAO scanDAOImpl){
        this.imageAnalyzerService = imageAnalyzerService;
        this.scanDAOImpl = scanDAOImpl;
    }

    public ScanDto scanImage(String image, Long id) {
        Timestamp scanTimestamp = new Timestamp(System.currentTimeMillis());
        com.logistic.reeasy.demo.scan.models.AnalyzedResult analyzedResult = imageAnalyzerService.scanImage(image);
        List<ScanBottleDetail> detailsBottlesList = analyzedResult.getDetails();

        if(detailsBottlesList == null || detailsBottlesList.isEmpty()) {
            log.error("Plastic bottles not detected in the image provided for user id {}", id);
            throw new PlasticBottleNotDetected("The image does not contain recyclable plastic bottles");
        }

        String resultImageBase64 = analyzedResult.getImage();
        if (resultImageBase64 == null || resultImageBase64.isEmpty()) {
            resultImageBase64 = image;
        }

        String pureBase64 = resultImageBase64;

        if (pureBase64.contains(",")) {
            pureBase64 = pureBase64.split(",")[1];
        }

        byte[] imageBytes = pureBase64.getBytes();


        detailsBottlesList.forEach(detail ->{
            ScanTableModel scanTableModel = new ScanTableModel(
                id,
                detail.getType().getId(),
                10,
                imageBytes,
                scanTimestamp
            );

            try{
                scanDAOImpl.insert(scanTableModel);
            }
            catch(Exception e){
                // TODO: Manejar errores SQL
                log.error("Error saving scan for user id {}: ", id, e);
                throw new RuntimeException("It happened an error on save scan", e);
            }
        });


        List<ScanBottleDetailDto> bottleDetails = detailsBottlesList.stream()
              .map(detail2 -> new ScanBottleDetailDto(detail2.getType(), detail2.getAmount()))
              .toList();

        return new ScanDto(scanTimestamp, bottleDetails, resultImageBase64);
    }
}