package com.logistic.reeasy.demo.scan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistic.reeasy.demo.scan.models.RequestScanDto;
import com.logistic.reeasy.demo.scan.models.ScanModel;
import com.logistic.reeasy.demo.scan.service.RecyclingService;

@RestController
@RequestMapping("/recycling")
public class RecyclingController {

  private final RecyclingService recyclingService;

  public RecyclingController(
      RecyclingService recyclingService) {
    this.recyclingService = recyclingService;
  }

  @GetMapping("/status")
  public String getRecyclingStatus() {
    return "Recycling service is running.";
  }

  @PostMapping("/scan")
  public ResponseEntity<ScanModel> scanImage(
      @RequestBody RequestScanDto request) {
    try {
      ScanModel result = recyclingService.scanImage(request.getImage(), request.getUserId());
      return ResponseEntity.ok(result);
    } catch (Exception e) {

      return ResponseEntity.status(500).build();
    }
  }
}
