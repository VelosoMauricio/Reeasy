package com.logistic.reeasy.demo.scan.dto;

import com.logistic.reeasy.demo.scan.models.BottleType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScanBottleDetailDto {
  private BottleType type;
  private int amount;
}
