package com.loan_org.underwriting_service.shared.exception.web;

import java.time.Instant;

import lombok.Builder;

/**
 * A custom exception handling payload to be sent and returned by the global
 * exception handler for easier debugging and clean logs.
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category DTO
 */
@Builder
public record CustomExceptionPayload(
    int code,
    String status,
    String message,
    Instant timestamp
) {}