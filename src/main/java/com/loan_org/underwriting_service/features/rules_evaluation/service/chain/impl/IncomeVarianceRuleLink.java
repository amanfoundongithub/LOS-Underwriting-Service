package com.loan_org.underwriting_service.features.rules_evaluation.service.chain.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.loan_org.underwriting_service.features.rules_evaluation.dto.RuleExecutionLog;
import com.loan_org.underwriting_service.features.rules_evaluation.model.RuleCategory;
import com.loan_org.underwriting_service.features.rules_evaluation.service.chain.UnderwritingRuleLinkAbstract;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingCase;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingStatus;

@Component
@Order(2)
public class IncomeVarianceRuleLink extends UnderwritingRuleLinkAbstract {

    public IncomeVarianceRuleLink() {
        super("INCOME_VARIANCE", RuleCategory.IDENTITY_AND_FRAUD);
    }

    @Override
    protected boolean executeRule(UnderwritingCase underwritingCase, RuleExecutionLog executionLog) {
        double declared = underwritingCase.getApplicantSnapshot().getDeclaredMonthlyIncome();
        double verified = underwritingCase.getApplicantSnapshot().getVerifiedMonthlyIncome();
        
        executionLog.setObservedValue("Verified:" + verified);
        executionLog.setThresholdValue("Declared:" + declared + ". So, >=70% of this value. Threshold: " + declared * 0.70);
        if (verified < (declared * 0.70)) { 
            executionLog.setPassed(false);
            underwritingCase.setStatus(UnderwritingStatus.REFERRED_TO_MANUAL);
        } else {
            executionLog.setPassed(true);
        }
        return false;
    }
    
}
