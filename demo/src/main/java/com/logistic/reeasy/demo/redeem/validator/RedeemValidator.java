package com.logistic.reeasy.demo.redeem.validator;

import com.logistic.reeasy.demo.common.exception.custom.InvalidExchangeException;
import com.logistic.reeasy.demo.coupons.dto.CouponDto;
import com.logistic.reeasy.demo.redeem.service.RedeemCouponService;
import com.logistic.reeasy.demo.users.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@Slf4j
public class RedeemValidator {
    public void validateExchange(UserDto user, CouponDto couponDto) {

        if(user.getPoints() < couponDto.getPrice()){
            log.warn("User with id {} does not have enough points to redeem coupon with id {}.", user.getId(), couponDto.getId());
            throw new InvalidExchangeException("User does not have enough points to redeem this coupon.");
        }

        if(LocalDate.now().isAfter(couponDto.getExpiration_date())){
            log.warn("User with id {} cannot redeem a expired coupon with id {}.", user.getId(), couponDto.getId());
            throw new InvalidExchangeException("Coupon has expired and cannot be redeemed.");
        }

        if(couponDto.getAmount() == 0){
            log.warn("User with id {} cannot redeem a coupon with id {} that is out of stock.", user.getId(), couponDto.getId());
            throw new InvalidExchangeException("Coupon is out of stock and cannot be redeemed.");
        }

        // TODO: Agregar lógico de negocio que el cupón debe pertener a la misma empresa asociada de el usuario
    }
}
