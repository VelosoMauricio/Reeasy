package com.logistic.reeasy.demo.coupons.di;

import com.logistic.reeasy.demo.coupons.validator.CouponValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouponValidatorConfig {

    @Bean
    public CouponValidator couponValidator() {
        return new CouponValidator();
    }
}
