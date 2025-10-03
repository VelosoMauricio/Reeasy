package com.logistic.reeasy.demo.common.exception.custom;

public class InvalidApiKeyException extends RuntimeException{
    public InvalidApiKeyException(String message) {
        super(message);
    }
}
