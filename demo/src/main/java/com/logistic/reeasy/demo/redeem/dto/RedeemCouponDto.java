package com.logistic.reeasy.demo.redeem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RedeemCouponDto {
    private String fullname;
    private String email;
    private Long couponId;
    private Timestamp timestamp;
    private LocalDate expirationDate;
    private String link;
    private int price;
}
