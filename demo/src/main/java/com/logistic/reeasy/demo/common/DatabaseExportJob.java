package com.logistic.reeasy.demo.common;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DatabaseExportJob {
    private final DatabaseExporter exporter;
    
    public DatabaseExportJob(DatabaseExporter exporter){
        this.exporter = exporter; 
    }

    @Scheduled(fixedRate = 30000)
    public void exportPeriodically() {
        try {
            exporter.exportDbToJson();
            System.out.println("nasheee");
            log.info("json de db actualizado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
