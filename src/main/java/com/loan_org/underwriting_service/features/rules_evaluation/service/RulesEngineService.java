package com.loan_org.underwriting_service.features.rules_evaluation.service;

import com.loan_org.underwriting_service.shared.domain.model.UnderwritingCase;

public interface RulesEngineService {
    UnderwritingCase execute(UnderwritingCase underwritingCase);
}
