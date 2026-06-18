package com.logistic.reeasy.demo.scan.models;

import java.util.List;
import lombok.Data;

@Data
public class YoloDetection {
    private String type;
    private List<Integer> xyxy;
}
