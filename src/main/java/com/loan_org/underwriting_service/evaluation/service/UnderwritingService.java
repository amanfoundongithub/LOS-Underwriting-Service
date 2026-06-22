package com.loan_org.underwriting_service.evaluation.service;

import java.util.Optional;

import com.loan_org.underwriting_service.evaluation.model.UnderwritingCase;
import com.loan_org.underwriting_service.evaluation.web.dto.UnderwritingTriggerRequest;
import com.loan_org.underwriting_service.evaluation.web.dto.UnderwritingTriggerResponse;

public interface UnderwritingService {
    UnderwritingTriggerResponse create(UnderwritingTriggerRequest request);
    UnderwritingCase            process(String applicationId);
    Optional<UnderwritingCase>            findByApplicationId(String applicationId);
}
