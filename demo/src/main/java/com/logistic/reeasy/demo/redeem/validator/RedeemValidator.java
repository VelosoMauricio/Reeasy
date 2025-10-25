package com.logistic.reeasy.demo.redeem.validator;

import com.logistic.reeasy.demo.common.exception.custom.InvalidExchangeException;
import com.logistic.reeasy.demo.coupons.dto.CouponDto;
import com.logistic.reeasy.demo.users.dto.UserDto;

import java.time.LocalDate;

public class RedeemValidator {

    public void validateExchange(UserDto user, CouponDto couponDto) {

        if(user.getPoints() < couponDto.getPrice()){
            throw new InvalidExchangeException("User does not have enough points to redeem this coupon.");
        }

        if(LocalDate.now().isAfter(couponDto.getExpiration_date())){
            throw new InvalidExchangeException("Coupon has expired and cannot be redeemed.");
        }

        if(couponDto.getAmount() == 0){
            throw new InvalidExchangeException("Coupon is out of stock and cannot be redeemed.");
        }

        // TODO: Agregar lógico de negocio que el cupón debe pertener a la misma empresa asociada de el usuario
    }
}
