package com.logistic.reeasy.demo.scan.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestScanDto {
  public String image;
  public Long userId;
}
