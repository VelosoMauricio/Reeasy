package com.logistic.reeasy.demo.common.exception.custom;

import com.logistic.reeasy.demo.common.exception.BaseApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCouponFormatException extends BaseApiException {

    private static final String ERROR_CODE = "COUPONS-001";

    public InvalidCouponFormatException(String message) {
        super(ERROR_CODE, message);
    }
}
