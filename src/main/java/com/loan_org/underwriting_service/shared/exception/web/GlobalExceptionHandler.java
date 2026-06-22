package com.loan_org.underwriting_service.shared.exception.web;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;


/**
 * A global exception handler to handle all kinds of exceptions related
 * to the processing of any API on this service.
 * 
 * Catches and applies all exception into a readable class, for easier debugging and
 * detailed analysis.
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category Exception Handler Service
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    


    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionPayload> exception(
        Exception ex
    ) {
        return createPayload(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<CustomExceptionPayload> createPayload(Exception ex, HttpStatus httpStatus) {
        log.warn("Encountered exception {} during execution. Reason: {}",
            ex.getClass().getName(), ex.getMessage()
        );

        return ResponseEntity.status(httpStatus).body(CustomExceptionPayload.builder()
        .code(httpStatus.value())
        .status(httpStatus.getReasonPhrase())
        .message(ex.getMessage())
        .timestamp(Instant.now())
        .build());
    }
    
}
