package com.logistic.reeasy.demo.Coupons.service;

import org.springframework.stereotype.Service;

import com.logistic.reeasy.demo.Coupons.iface.AdminDAO;
import com.logistic.reeasy.demo.Coupons.models.CouponModel;

@Service
public class AdminService{

    private final AdminDAO adminDAO;

    public AdminService(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    public CouponModel addCoupon(CouponModel coupon){
        
        return adminDAO.addCoupon(coupon);

    }
}
