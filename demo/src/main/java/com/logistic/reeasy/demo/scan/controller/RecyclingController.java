package com.logistic.reeasy.demo.scan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistic.reeasy.demo.scan.dto.RequestScanDto;
import com.logistic.reeasy.demo.scan.dto.ScanDto;
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
  public ResponseEntity getRecyclingStatus() {
    return ResponseEntity.ok("Recycling service is running.");
  }

  @CrossOrigin(origins = "http://localhost:5173")
  @PostMapping("/scan")
  public ResponseEntity<ScanDto> scanImage(@RequestBody RequestScanDto request) {
    ScanDto result = recyclingService.scanImage(request.getImage(), request.getUserId());
    return ResponseEntity.ok(result);
  }
}
