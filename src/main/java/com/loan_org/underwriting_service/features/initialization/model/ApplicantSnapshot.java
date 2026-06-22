package com.loan_org.underwriting_service.features.initialization.model;

import com.loan_org.underwriting_service.shared.domain.model.EmploymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Builds the applicant's profile snapshot for underwriting processing.
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantSnapshot {
    private double declaredMonthlyIncome;
    private double verifiedMonthlyIncome;
    private EmploymentStatus employmentStatus;
    private boolean identityVerified;
}