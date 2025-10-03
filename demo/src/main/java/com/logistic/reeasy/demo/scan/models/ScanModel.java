package com.logistic.reeasy.demo.scan.models;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScanModel {
    private Long userId;
    private LocalDate date;
    private byte[] image;
    private List<ScanBottleDetail> data;
}
