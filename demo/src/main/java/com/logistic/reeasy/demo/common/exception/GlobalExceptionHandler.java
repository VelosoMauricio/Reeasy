package com.logistic.reeasy.demo.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.logistic.reeasy.demo.common.exception.custom.PlasticBottleNotDetected;


@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PlasticBottleNotDetected.class)
  public ResponseEntity<ErrorResponse> handlePlasticBottleNotDetected(PlasticBottleNotDetected ex) {
    ErrorResponse errorDetails = new ErrorResponse(400, "Plastic Bottle Not Detected", ex.getMessage());
    
    return new ResponseEntity<>(errorDetails, org.springframework.http.HttpStatus.BAD_REQUEST);
  }
}
