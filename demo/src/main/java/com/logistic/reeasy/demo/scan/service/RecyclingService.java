package com.logistic.reeasy.demo.scan.service;

import java.sql.Timestamp;
import java.util.List;

import com.logistic.reeasy.demo.scan.models.ScanTableModel;
import org.springframework.stereotype.Service;

import com.logistic.reeasy.demo.common.exception.custom.PlasticBottleNotDetected;
import com.logistic.reeasy.demo.scan.dto.ScanBottleDetailDto;
import com.logistic.reeasy.demo.scan.dto.ScanDto;
import com.logistic.reeasy.demo.scan.iface.ScanDAO;
import com.logistic.reeasy.demo.scan.models.ScanBottleDetail;

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
        List<ScanBottleDetail> detailsBottlesList = imageAnalyzerService.scanImage(image);

        if(detailsBottlesList == null || detailsBottlesList.isEmpty()) {
          throw new PlasticBottleNotDetected("The image does not contain recyclable plastic bottles");
        }

        String pureBase64 = image;

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
                scanDAOImpl.insert(scanTableModel, "Scans");
            }
            catch(Exception e){
                // TODO: Manejar errores SQL

                throw new RuntimeException("It happened an error on save scan", e);
            }
        });


        List<ScanBottleDetailDto> bottleDetails = detailsBottlesList.stream()
              .map(detail2 -> new ScanBottleDetailDto(detail2.getType(), detail2.getAmount()))
              .toList();

        return new ScanDto(scanTimestamp, bottleDetails);
    }
}