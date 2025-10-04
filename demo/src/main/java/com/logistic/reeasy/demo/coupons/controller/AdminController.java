package com.logistic.reeasy.demo.coupons.controller;

import java.util.HashMap;
import java.util.Map;

import com.logistic.reeasy.demo.coupons.dto.CouponDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.logistic.reeasy.demo.coupons.models.CouponModel;
import com.logistic.reeasy.demo.coupons.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/status")
    public ResponseEntity getRecyclingStatus() {
        return ResponseEntity.ok("Admin service is running.");
    }

    @PostMapping("/coupon")
    //@PreAuthorize("hasRole('ADMIN')") -> Para el futuro cuando halla token JWT :)
    public ResponseEntity<CouponDto> createCoupon(@RequestBody CouponModel coupon) {
        CouponDto createdCoupon = adminService.addCoupon(coupon);
        return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
    }
}
