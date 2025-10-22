package com.logistic.reeasy.demo.coupons.validator;

import com.logistic.reeasy.demo.common.exception.custom.InvalidCouponFormatException;
import com.logistic.reeasy.demo.coupons.models.CouponModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CouponValidator {

    public void validate(CouponModel coupon){

        if(coupon.amount <= 0){
            throw new InvalidCouponFormatException("The amount of coupons must be greater than zero");
        }

        if(coupon.price < 0){
            throw new InvalidCouponFormatException("The price of the coupon cannot be negative");
        }

        if(coupon.expiration_date.isBefore(LocalDate.now())){
            throw new InvalidCouponFormatException("The expiration date cannot be in the past");
        }

        if(coupon.description.isEmpty()){
            throw new InvalidCouponFormatException("The description cannot be empty");
        }

    }
}
