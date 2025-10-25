package com.logistic.reeasy.demo.redeem.di;

import com.logistic.reeasy.demo.redeem.validator.RedeemValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedeemCouponValidatorConfig {

    @Bean
    public RedeemValidator redeemValidator() {
        return new RedeemValidator();
    }
}
