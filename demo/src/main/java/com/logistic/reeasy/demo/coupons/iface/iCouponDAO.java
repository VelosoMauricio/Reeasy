package com.logistic.reeasy.demo.coupons.iface;

import com.logistic.reeasy.demo.common.abs.InterfaceDAO;
import com.logistic.reeasy.demo.coupons.models.CouponModel;

public interface iCouponDAO extends InterfaceDAO<CouponModel> {
    public int redeemOne(Long couponId);
}
