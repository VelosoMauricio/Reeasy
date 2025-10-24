package com.logistic.reeasy.demo.redeem.di;

import com.logistic.reeasy.demo.redeem.dao.RedeemCouponDAOImpl;
import com.logistic.reeasy.demo.redeem.iface.RedeemCouponDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;

@Configuration
public class RedeemCouponConfig {

    @Bean
    public RedeemCouponDAO redeemCouponDAO(Sql2o sql2o) {
        return new RedeemCouponDAOImpl(sql2o);
    }
}
