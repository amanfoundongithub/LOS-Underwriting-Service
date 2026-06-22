package com.loan_org.underwriting_service.evaluation.web.dto;

import java.util.Map;

public record UnderwritingTriggerRequest(
    String correlationId,        // The unique ID matching the Core LOS Application
    String applicationId,          // Unique ID of the customer
    String employmentStatus,     // e.g., "SALARIED", "SELF_EMPLOYED"
    double declaredMonthlyIncome,
    double verifiedMonthlyIncome,
    boolean identityVerified,
    Map<String, Object> metadata // Open map for any extra verification signals passed along
) {}
