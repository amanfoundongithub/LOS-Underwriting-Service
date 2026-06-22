package com.loan_org.underwriting_service.evaluation.model;

import lombok.Builder;

@Builder
public record ApplicantSnapshot(
    double declaredMonthlyIncome,
    double verifiedMonthlyIncome,
    String employmentStatus,
    boolean identityVerified
) {}