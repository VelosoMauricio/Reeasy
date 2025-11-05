package com.logistic.reeasy.demo.coupons.service;

import com.logistic.reeasy.demo.common.exception.BaseApiException;
import com.logistic.reeasy.demo.coupons.dto.CouponDto;
import com.logistic.reeasy.demo.coupons.validator.CouponValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.logistic.reeasy.demo.coupons.iface.iCouponDAO;
import com.logistic.reeasy.demo.coupons.models.CouponModel;

@Slf4j
@Service
public class CouponService {

    private final iCouponDAO iCouponDAO;
    private final CouponValidator couponValidator;

    public CouponService(iCouponDAO iCouponDAO, CouponValidator couponValidator) {
        this.iCouponDAO = iCouponDAO;
        this.couponValidator = couponValidator;
    }

    public CouponDto addCoupon(CouponModel coupon){

        log.info("Attempting to add a new coupon");
        log.info("Validating coupon data");
        couponValidator.validate(coupon);

        log.info("Coupon data is valid. Proceeding to add the coupon");

        try{
            CouponModel response = iCouponDAO.insert(coupon);

            System.out.println(response);

            log.info("Coupon added successfully with id: " + response.getCoupon_id());

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
            log.error("ERROR ON addCoupon: " + e.getMessage());
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
            log.error("ERROR ON findCouponById with id " + couponId + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    public void substractOne(Long couponId){
        try{
            iCouponDAO.findById(couponId, "coupon_id");
            iCouponDAO.redeemOne(couponId);
        }catch (BaseApiException e){
            log.error("API Error on substract one coupon with id " + couponId + ": " + e.getMessage());
            throw e;
        }
        catch (Exception e){
            log.error("ERROR ON substract one coupon with id " + couponId + ": " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
