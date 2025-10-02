package com.logistic.reeasy.demo.Coupons.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistic.reeasy.demo.Coupons.models.CouponModel;
import com.logistic.reeasy.demo.Coupons.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {


    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/coupons")
    public ResponseEntity<?> createCoupon(@RequestBody CouponModel coupon) {
        
        try {
            CouponModel createdCoupon = adminService.addCoupon(coupon);

            
            return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);

        } catch (Exception e) {
            // --- FORMA MODERNA Y RECOMENDADA PARA ERROR ---
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ocurrió un error interno al procesar la solicitud.");
            errorResponse.put("message", e.getMessage());

            
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
}