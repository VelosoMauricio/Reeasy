package com.logistic.reeasy.demo.coupons.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponModel{
    public LocalDate expiration_date;
    public int price;
    public int amount;
    public String description;
    public String link;
    public String image;
    public String enterprise_cuit;
    public Long coupon_id;
}