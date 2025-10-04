package com.logistic.reeasy.demo.coupons.dao;


import com.logistic.reeasy.demo.common.abs.AbstractDAO;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import com.logistic.reeasy.demo.coupons.models.CouponModel;
import com.logistic.reeasy.demo.coupons.iface.AdminDAO;

@Repository
public class AdminDAOimpl extends AbstractDAO implements AdminDAO{

    public AdminDAOimpl(Sql2o sql2o) {
        super(sql2o);
    }
}