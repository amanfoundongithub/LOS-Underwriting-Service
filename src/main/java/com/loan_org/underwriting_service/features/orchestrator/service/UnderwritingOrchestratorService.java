package com.loan_org.underwriting_service.features.orchestrator.service;

import com.loan_org.underwriting_service.shared.domain.model.UnderwritingCase;

public interface UnderwritingOrchestratorService {
    UnderwritingCase            process(String applicationId);
}
