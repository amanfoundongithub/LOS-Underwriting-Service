package com.loan_org.underwriting_service.features.initialization.dto;

import java.util.Map;

import com.loan_org.underwriting_service.shared.domain.model.EmploymentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Record model for taking HTTP input to trigger Underwriting task for the Core 
 * application.
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category DTO
 */
public record UnderwritingTriggerRequest(

    @NotBlank(message = "Correlation ID is required for distributed traceability.")
    String correlationId,

    @NotBlank(message = "Application ID is required for tracking the application.")
    String applicationId,

    @NotBlank(message = "Customer ID is required to track the customer requiring the loan.")
    String customerId,

    @NotNull(message = "Employment Status is required.")
    EmploymentStatus employmentStatus,   

    @PositiveOrZero(message = "Declared Monthly Income >= 0")
    double declaredMonthlyIncome,

    @PositiveOrZero(message = "Verified Monthly Income >= 0")
    double verifiedMonthlyIncome,

    boolean identityVerified,
    Map<String, Object> metadata
    
) {}
