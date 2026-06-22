package com.loan_org.underwriting_service.features.rules_evaluation.service.chain.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.loan_org.underwriting_service.features.rules_evaluation.dto.RuleExecutionLog;
import com.loan_org.underwriting_service.features.rules_evaluation.model.RuleCategory;
import com.loan_org.underwriting_service.features.rules_evaluation.service.chain.UnderwritingRuleLinkAbstract;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingCase;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingRiskTier;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingStatus;

@Component
@Order(1)
public class CreditScoreRuleLink extends UnderwritingRuleLinkAbstract {

    public static final int MINIMUM_CREDIT_SCORE_REQUIRED = 600;

    public CreditScoreRuleLink() {
        super("CREDIT_SCORE_CHECK", RuleCategory.CREDIT_HISTORY);
    }

    @Override
    protected boolean executeRule(UnderwritingCase underwritingCase, RuleExecutionLog executionLog) {

        int score = underwritingCase.getBureauSnapshot().getCreditScore();
        boolean passed = score > MINIMUM_CREDIT_SCORE_REQUIRED || score < 0;

        executionLog.setObservedValue(String.valueOf(score));
        executionLog.setThresholdValue(">=" + MINIMUM_CREDIT_SCORE_REQUIRED + "or < 0");
        executionLog.setPassed(passed);

        if(!passed) {
            underwritingCase.setRiskTier(UnderwritingRiskTier.TIER_F);
            underwritingCase.setStatus(UnderwritingStatus.DECLINED);
        }
        if(score < 0) {
            underwritingCase.setRiskTier(UnderwritingRiskTier.NEW_TO_CREDIT);
        }

        return passed == false;
    
    }
    
}
