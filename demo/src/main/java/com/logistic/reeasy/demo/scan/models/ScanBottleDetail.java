package com.logistic.reeasy.demo.scan.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScanBottleDetail {
    private int amount;

    // @JsonProperty("bottleType")
    private BottleType type;
}
