package com.logistic.reeasy.demo.common.exception.custom;

public class GoogleApiServiceException extends RuntimeException{
    public GoogleApiServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
