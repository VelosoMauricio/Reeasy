package com.logistic.reeasy.demo.coupons.dao;


import com.logistic.reeasy.demo.common.abs.AbstractDAO;
import com.logistic.reeasy.demo.coupons.models.CouponModel;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import com.logistic.reeasy.demo.coupons.iface.iCouponDAO;

@Repository
public class CouponDAOImpl extends AbstractDAO<CouponModel> implements iCouponDAO {

    public CouponDAOImpl(Sql2o sql2o) {
        super(CouponModel.class,"Coupons", sql2o);
    }
}