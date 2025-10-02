package com.logistic.reeasy.demo.scan.models;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScanModel {
    private LocalDate date;
    private String image;
    private Long userId;
    private List<ScanBottleDetail> data;
}
