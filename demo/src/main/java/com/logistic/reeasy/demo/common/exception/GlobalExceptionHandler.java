package com.logistic.reeasy.demo.common.exception;

import com.logistic.reeasy.demo.common.exception.custom.GoogleApiServiceException;
import com.logistic.reeasy.demo.common.exception.custom.InvalidApiKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.logistic.reeasy.demo.common.exception.custom.PlasticBottleNotDetected;


@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PlasticBottleNotDetected.class)
  public ResponseEntity<ErrorResponse> handlePlasticBottleNotDetected(PlasticBottleNotDetected ex) {

      ErrorResponse errorDetails = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Plastic Bottle Not Detected",
            ex.getMessage()
    );
    
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

    @ExceptionHandler(InvalidApiKeyException.class)
    public ResponseEntity<ErrorResponse> handleInvalidApiKey(InvalidApiKeyException ex) {

        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(), // 503
                "Invalid API KEY",
                ex.getMessage()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(GoogleApiServiceException.class)
    public ResponseEntity<ErrorResponse> handleGoogleApiServiceException(GoogleApiServiceException ex) {
        ErrorResponse errorDetails = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "It happend an error",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
