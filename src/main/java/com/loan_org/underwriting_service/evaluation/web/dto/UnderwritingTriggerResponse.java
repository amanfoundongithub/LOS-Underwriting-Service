package com.loan_org.underwriting_service.evaluation.web.dto;

import lombok.Builder;

@Builder
public record UnderwritingTriggerResponse(
    String caseId,
    String status,
    String message
) {}