package com.loan_org.underwriting_service.features.rules_evaluation.service.chain;

import com.loan_org.underwriting_service.shared.domain.model.UnderwritingCase;

public interface UnderwritingRuleLink {

    UnderwritingCase evaluate(UnderwritingCase underwritingCase);
    void setNext(UnderwritingRuleLink next);

}