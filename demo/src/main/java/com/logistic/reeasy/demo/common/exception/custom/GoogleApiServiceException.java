package com.logistic.reeasy.demo.common.exception.custom;

import com.logistic.reeasy.demo.common.exception.BaseApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class GoogleApiServiceException extends BaseApiException {
    private static final String ERROR_CODE = "SCAN-003";

    public GoogleApiServiceException(String message, Throwable cause) {
        super(ERROR_CODE, message);

        // TODO: Agregar algún sistema de login para el cause.
    }
}
