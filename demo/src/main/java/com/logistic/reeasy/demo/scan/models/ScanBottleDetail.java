package com.logistic.reeasy.demo.scan.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScanBottleDetail {
    private int amount;

    private BottleType type;
}
