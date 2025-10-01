package com.logistic.reeasy.demo.Scan.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScanBottleDetail {
    private int amount; 
    private BottleType type; 
}
