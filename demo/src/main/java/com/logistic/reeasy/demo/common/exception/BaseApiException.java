package com.logistic.reeasy.demo.common.exception;

// En /common/exception/
public abstract class BaseApiException extends RuntimeException {

    private final String errorCode;

    public BaseApiException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}