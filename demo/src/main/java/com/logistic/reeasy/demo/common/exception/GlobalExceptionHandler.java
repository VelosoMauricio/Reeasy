package com.logistic.reeasy.demo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;


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


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(HttpMessageNotReadableException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String ERROR_CODE = "JSON-001";
        String message = "JSON Format not valid";

        ErrorResponse body = new ErrorResponse(
                status.value(),
                ERROR_CODE,
                message
        );

        return new ResponseEntity<>(body, status);
    }
}
