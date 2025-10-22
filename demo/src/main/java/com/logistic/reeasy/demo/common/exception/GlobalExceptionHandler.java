package com.logistic.reeasy.demo.common.exception;

import com.logistic.reeasy.demo.common.exception.custom.GoogleApiServiceException;
import com.logistic.reeasy.demo.common.exception.custom.InvalidApiKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.logistic.reeasy.demo.common.exception.custom.PlasticBottleNotDetected;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseApiException.class)
    public ResponseEntity<ErrorResponse> handleBaseApiException(BaseApiException ex) {

        ResponseStatus statusAnnotation = ex.getClass().getAnnotation(ResponseStatus.class);

        HttpStatus status = statusAnnotation != null
                ? statusAnnotation.value()
                : HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse body = new ErrorResponse(
                status.value(),
                ex.getErrorCode(),
                ex.getMessage()
        );

        return new ResponseEntity<>(body, status);
    }
}
