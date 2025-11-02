package com.logistic.reeasy.demo.coupons.validator;

import com.logistic.reeasy.demo.common.exception.custom.InvalidCouponFormatException;
import com.logistic.reeasy.demo.coupons.models.CouponModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CouponValidator {

    private static final Logger log = LoggerFactory.getLogger(CouponValidator.class);

    public void validate(CouponModel coupon){

        if(coupon.amount <= 0){
            log.warn("Error on add coupon: The amount of coupons must be greater than zero");
            throw new InvalidCouponFormatException("The amount of coupons must be greater than zero");
        }

        if(coupon.price < 0){
            log.warn("Error on add coupon: The price of the coupon cannot be negative");
            throw new InvalidCouponFormatException("The price of the coupon cannot be negative");
        }

        if(coupon.expiration_date.isBefore(LocalDate.now())){
            log.warn("Error on add coupon: The expiration date cannot be in the past");
            throw new InvalidCouponFormatException("The expiration date cannot be in the past");
        }

        if(coupon.description.isEmpty()){
            log.warn("Error on add coupon: The description cannot be empty");
            throw new InvalidCouponFormatException("The description cannot be empty");
        }

    }
}
