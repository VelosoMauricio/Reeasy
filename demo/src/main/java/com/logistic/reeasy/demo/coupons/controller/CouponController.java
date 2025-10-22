package com.logistic.reeasy.demo.coupons.controller;

import com.logistic.reeasy.demo.coupons.dto.CouponDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.logistic.reeasy.demo.coupons.models.CouponModel;
import com.logistic.reeasy.demo.coupons.service.CouponService;

@RestController
@RequestMapping("/admin")
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/status")
    public ResponseEntity getRecyclingStatus() {
        return ResponseEntity.ok("Admin service is running.");
    }

    @PostMapping("/coupon")
    //@PreAuthorize("hasRole('ADMIN')") -> Para el futuro cuando halla token JWT :)
    public ResponseEntity<CouponDto> createCoupon(@RequestBody CouponModel coupon) {
        CouponDto createdCoupon = couponService.addCoupon(coupon);
        return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
    }
}
