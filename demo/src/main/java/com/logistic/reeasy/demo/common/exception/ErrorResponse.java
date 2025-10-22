package com.logistic.reeasy.demo.common.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private int statusCode;
    private String errorCode;
    private String message;

    public ErrorResponse(int statusCode, String errorCode, String message) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
    }
}
