package com.logistic.reeasy.demo.common.exception.custom;

import com.logistic.reeasy.demo.common.exception.BaseApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidExchangeException extends BaseApiException {
    private static final String ERROR_CODE = "REDEEM-001";

    public InvalidExchangeException(String message) {
        super(ERROR_CODE, message);
    }
}
