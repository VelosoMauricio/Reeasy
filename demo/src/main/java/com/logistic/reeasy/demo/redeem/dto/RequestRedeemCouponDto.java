package com.logistic.reeasy.demo.redeem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestRedeemCouponDto {
    private Long userId;
    private Long couponId;
}
