package com.logistic.reeasy.demo.scan.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyzedResult {
    private String image;
    private List<ScanBottleDetail> details;
}
