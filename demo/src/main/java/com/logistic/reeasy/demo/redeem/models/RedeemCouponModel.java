package com.logistic.reeasy.demo.redeem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class RedeemCouponModel {
    private Long coupon_id;
    private Long user_id;
    private Timestamp timestamp;
}
