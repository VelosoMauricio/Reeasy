package com.logistic.reeasy.demo.redeem.controller;

import com.logistic.reeasy.demo.redeem.dto.RedeemCouponDto;
import com.logistic.reeasy.demo.redeem.dto.RequestRedeemCouponDto;
import com.logistic.reeasy.demo.redeem.service.RedeemCouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RedeemCouponController {
    private final RedeemCouponService redeemCouponService;

    public RedeemCouponController(RedeemCouponService redeemCouponService) {
        this.redeemCouponService = redeemCouponService;
    }

    @GetMapping("/status")
    public ResponseEntity getRecyclingStatus() {
        return ResponseEntity.ok("Redeem Coupon service is running.");
    }

    @PostMapping("/redeem")
    public ResponseEntity<RedeemCouponDto> redeemCoupon(@RequestBody RequestRedeemCouponDto request) {
        RedeemCouponDto result = redeemCouponService.redeemCoupon(request.getUserId(), request.getCouponId());
        return ResponseEntity.ok(result);
    }
}
