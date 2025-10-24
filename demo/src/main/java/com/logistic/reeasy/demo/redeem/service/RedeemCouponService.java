package com.logistic.reeasy.demo.redeem.service;

import com.logistic.reeasy.demo.coupons.dto.CouponDto;
import com.logistic.reeasy.demo.coupons.service.CouponService;
import com.logistic.reeasy.demo.redeem.dto.RedeemCouponDto;
import org.springframework.stereotype.Service;

@Service
public class RedeemCouponService {

    private final CouponService couponService;
    // private final UserService userService;

    public RedeemCouponService(CouponService couponService) {
        this.couponService = couponService;
    }


    public RedeemCouponDto redeemCoupon(Long userId, Long couponId) {

        CouponDto res = couponService.findCouponById(couponId);

        System.out.println(res);

        return null;
    }


}
