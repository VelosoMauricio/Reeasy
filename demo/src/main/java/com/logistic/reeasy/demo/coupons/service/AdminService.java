package com.logistic.reeasy.demo.coupons.service;

import com.logistic.reeasy.demo.coupons.dto.CouponDto;
import org.springframework.stereotype.Service;

import com.logistic.reeasy.demo.coupons.iface.AdminDAO;
import com.logistic.reeasy.demo.coupons.models.CouponModel;

import java.time.LocalDate;

@Service
public class AdminService{

    private final AdminDAO adminDAO;

    public AdminService(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    public CouponDto addCoupon(CouponModel coupon){

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
