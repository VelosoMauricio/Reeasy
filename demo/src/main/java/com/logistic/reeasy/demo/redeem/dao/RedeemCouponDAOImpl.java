package com.logistic.reeasy.demo.redeem.dao;

import com.logistic.reeasy.demo.common.abs.AbstractDAO;
import com.logistic.reeasy.demo.redeem.iface.RedeemCouponDAO;
import com.logistic.reeasy.demo.redeem.models.RedeemCouponModel;
import org.sql2o.Sql2o;

public class RedeemCouponDAOImpl extends AbstractDAO implements RedeemCouponDAO {
    public RedeemCouponDAOImpl(Sql2o sql2o) {
        super(RedeemCouponModel.class,"RedeemCoupon", sql2o);
    }
}
