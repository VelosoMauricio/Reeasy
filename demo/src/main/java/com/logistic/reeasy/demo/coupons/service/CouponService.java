package com.logistic.reeasy.demo.coupons.service;

import com.logistic.reeasy.demo.common.exception.custom.InvalidCouponFormatException;
import com.logistic.reeasy.demo.coupons.dto.CouponDto;
import com.logistic.reeasy.demo.coupons.validator.CouponValidator;
import org.springframework.stereotype.Service;

import com.logistic.reeasy.demo.coupons.iface.AdminDAO;
import com.logistic.reeasy.demo.coupons.models.CouponModel;

import java.time.LocalDate;

@Service
public class CouponService {

    private final AdminDAO adminDAO;
    private final CouponValidator couponValidator;

    public CouponService(AdminDAO adminDAO, CouponValidator couponValidator) {
        this.adminDAO = adminDAO;
        this.couponValidator = couponValidator;
    }

    public CouponDto addCoupon(CouponModel coupon){

        couponValidator.validate(coupon);

        try{
            CouponModel response = adminDAO.insert(coupon, "Coupons");

            return new CouponDto(
                    response.getExpiration_date(),
                    response.getPrice(),
                    response.getAmount(),
                    response.getDescription(),
                    response.getLink(),
                    response.getImage()
            );

        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
