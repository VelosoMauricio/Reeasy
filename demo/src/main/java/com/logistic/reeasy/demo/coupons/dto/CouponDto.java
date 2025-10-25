package com.logistic.reeasy.demo.coupons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CouponDto {
    private LocalDate expiration_date;
    private int price;
    private int amount;
    private String description;
    private String link;
    private String image;
}
