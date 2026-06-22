package com.loan_org.underwriting_service.features.initialization.service;

import com.loan_org.underwriting_service.features.initialization.dto.UnderwritingTriggerRequest;
import com.loan_org.underwriting_service.features.initialization.dto.UnderwritingTriggerResponse;

public interface UnderwritingInitializationService {
    UnderwritingTriggerResponse initialize(UnderwritingTriggerRequest request);
}
