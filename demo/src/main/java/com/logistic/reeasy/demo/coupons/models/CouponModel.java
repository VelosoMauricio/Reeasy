package com.logistic.reeasy.demo.Coupons.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CouponModel{
    public int coupon_id;
    public LocalDate expiration_date;
    public int price;
    public int amount;
    public String description;
    public String link;
    public String image;
    public Long Enterprise_cuit;
}