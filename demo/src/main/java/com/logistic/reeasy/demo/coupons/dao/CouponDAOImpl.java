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

    @Override
    public int redeemOne(Long couponId) {
        String sql = "UPDATE Coupons SET amount = amount - 1 WHERE coupon_id = :id AND amount > 0";

        try (var con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", couponId)
                    .executeUpdate()
                    .getResult();
        }
    }
}