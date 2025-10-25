package com.logistic.reeasy.demo.redeem.service;

import com.logistic.reeasy.demo.common.exception.BaseApiException;
import com.logistic.reeasy.demo.coupons.dto.CouponDto;
import com.logistic.reeasy.demo.coupons.models.CouponModel;
import com.logistic.reeasy.demo.coupons.service.CouponService;
import com.logistic.reeasy.demo.redeem.dao.RedeemCouponDAOImpl;
import com.logistic.reeasy.demo.redeem.dto.RedeemCouponDto;
import com.logistic.reeasy.demo.redeem.iface.RedeemCouponDAO;
import com.logistic.reeasy.demo.redeem.models.RedeemCouponModel;
import com.logistic.reeasy.demo.redeem.validator.RedeemValidator;
import com.logistic.reeasy.demo.users.dto.UserDto;
import com.logistic.reeasy.demo.users.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class RedeemCouponService {

    private final CouponService couponService;
    private final UserService userService;
    private final RedeemCouponDAO redeemCouponDAO;
    private final RedeemValidator redeemValidator;

    public RedeemCouponService(CouponService couponService, UserService userService, RedeemCouponDAO redeemCouponDAO, RedeemValidator redeemValidator) {
        this.couponService = couponService;
        this.userService = userService;
        this.redeemCouponDAO = redeemCouponDAO;
        this.redeemValidator = redeemValidator;
    }

    @Transactional
    public RedeemCouponDto redeemCoupon(Long userId, Long couponId) {

        CouponDto coupon;
        UserDto user;

        try{
            coupon = couponService.findCouponById(couponId);
            user = userService.findUserById(userId);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        redeemValidator.validateExchange(user, coupon);

        try{

            RedeemCouponModel cInsert =
                new RedeemCouponModel(
                    userId,
                    couponId,
                    new Timestamp(System.currentTimeMillis())
                );

            RedeemCouponModel cRes = redeemCouponDAO.insert(cInsert); // Insertamos el canje
            couponService.substractOne(couponId); // Restamos uno al cupón
            userService.substractPoints(userId, coupon.getPrice()); // Restamos los puntos al usuario

            return new RedeemCouponDto(
                    user.getFullname(),
                    user.getEmail(),
                    cRes.getCoupon_id(),
                    new Timestamp(System.currentTimeMillis()),
                    coupon.getExpiration_date(),
                    coupon.getLink(),
                    coupon.getPrice()
            );
        }
        catch (BaseApiException e){
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
