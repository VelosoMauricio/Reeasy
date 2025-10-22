package com.logistic.reeasy.demo.common.exception.custom;

import com.logistic.reeasy.demo.common.exception.BaseApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlasticBottleNotDetected extends BaseApiException {

    private static final String ERROR_CODE = "SCAN-002";

    public PlasticBottleNotDetected(String message) {
        super(ERROR_CODE, message);
    }
}