package com.logistic.reeasy.demo.Scan.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/recycling")
public class RecyclingController {

    @GetMapping("/recycling/status")
    public String getRecyclingStatus() {
        return "Recycling service is running.";
    }

    @PostMapping("/scanImage")
    public ResponseEntity<NullPointerException> scanImage(){
        return null; 
    }
}