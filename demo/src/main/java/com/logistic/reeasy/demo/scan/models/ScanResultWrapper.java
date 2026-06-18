package com.logistic.reeasy.demo.scan.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScanResultWrapper {

    @JsonProperty("image")
    private String image;

    @JsonProperty("details")
    private List<YoloDetection> details;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<YoloDetection> getDetails() {
        return details;
    }

    public void setDetails(List<YoloDetection> details) {
        this.details = details;
    }
}