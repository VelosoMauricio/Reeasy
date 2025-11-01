package com.logistic.reeasy.demo.coupons.service;

import com.logistic.reeasy.demo.common.exception.BaseApiException;
import com.logistic.reeasy.demo.coupons.dto.CouponDto;
import com.logistic.reeasy.demo.coupons.validator.CouponValidator;
import org.springframework.stereotype.Service;

import com.logistic.reeasy.demo.coupons.iface.iCouponDAO;
import com.logistic.reeasy.demo.coupons.models.CouponModel;

@Service
public class CouponService {

    private final iCouponDAO iCouponDAO;
    private final CouponValidator couponValidator;

    public CouponService(iCouponDAO iCouponDAO, CouponValidator couponValidator) {
        this.iCouponDAO = iCouponDAO;
        this.couponValidator = couponValidator;
    }

    public CouponDto addCoupon(CouponModel coupon){

        couponValidator.validate(coupon);

        try{
            CouponModel response = iCouponDAO.insert(coupon);

            return new CouponDto(
                    response.getCoupon_id(),
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

    public CouponDto findCouponById(Long couponId){

        try{
            CouponModel response = iCouponDAO.findById(couponId, "coupon_id");

            return new CouponDto(
                    response.getCoupon_id(),
                    response.getExpiration_date(),
                    response.getPrice(),
                    response.getAmount(),
                    response.getDescription(),
                    response.getLink(),
                    response.getImage()
            );

        }
        catch (Exception e){

            e.printStackTrace();

            throw new RuntimeException(e.getMessage());
        }

    }

    public void substractOne(Long couponId){
        try{
            iCouponDAO.findById(couponId, "coupon_id");
            iCouponDAO.redeemOne(couponId);
        }catch (BaseApiException e){
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
