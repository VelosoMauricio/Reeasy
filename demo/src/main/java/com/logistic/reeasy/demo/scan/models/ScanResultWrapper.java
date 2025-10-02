package com.logistic.reeasy.demo.scan.models; // O donde estén tus modelos

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty; // Asegúrate de tener esta importación

public class ScanResultWrapper {

    @JsonProperty("details")
    private List<ScanBottleDetail> details;

    // Getters y Setters
    public List<ScanBottleDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ScanBottleDetail> details) {
        this.details = details;
    }
}