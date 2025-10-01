package com.logistic.reeasy.demo.Scan.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScanModel {
    private LocalDate date;
    private String image;
    
    @Id()
    @ManyToOne
    private Long userId; 
    private List<ScanBottleDetail> data; 
}
