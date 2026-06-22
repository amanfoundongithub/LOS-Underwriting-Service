package com.loan_org.underwriting_service.evaluation.external;

import com.loan_org.underwriting_service.evaluation.model.BureauSnapshot;

public interface BureauIntegrationService {
    BureauSnapshot fetchProfile(String applicationId);
}