package com.logistic.reeasy.demo.scan.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScanDto {
  private LocalDate date;
  private List<ScanBottleDetailDto> data;
}
