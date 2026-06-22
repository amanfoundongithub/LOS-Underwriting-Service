package com.loan_org.underwriting_service.features.initialization.dto;

import com.loan_org.underwriting_service.shared.domain.model.UnderwritingStatus;

import lombok.Builder;

/**
 * Response to the user's request to trigger underwriting for the given applicationId
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category DTO
 */
@Builder
public record UnderwritingTriggerResponse(
    String caseId,
    String applicationId,
    String correlationId,
    UnderwritingStatus status,
    long estimatedProcessingTimeMs,
    String message
) {}