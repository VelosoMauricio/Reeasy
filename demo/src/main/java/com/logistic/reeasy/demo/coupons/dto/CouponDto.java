package com.logistic.reeasy.demo.coupons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CouponDto {
    public LocalDate expiration_date;
    public int price;
    public int amount;
    public String description;
    public String link;
    public String image;
}
