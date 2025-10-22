package com.logistic.reeasy.demo.common.exception.custom;

import com.logistic.reeasy.demo.common.exception.BaseApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class InvalidApiKeyException extends BaseApiException {

    private static final String ERROR_CODE = "SCAN-001";

    public InvalidApiKeyException(String message) {
        super(ERROR_CODE, message);
    }
}
