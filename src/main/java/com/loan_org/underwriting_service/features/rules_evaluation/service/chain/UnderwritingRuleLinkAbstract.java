package com.loan_org.underwriting_service.features.rules_evaluation.service.chain;

import java.time.Instant;

import com.loan_org.underwriting_service.features.rules_evaluation.dto.RuleExecutionLog;
import com.loan_org.underwriting_service.features.rules_evaluation.model.RuleCategory;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingCase;

public abstract class UnderwritingRuleLinkAbstract implements UnderwritingRuleLink {

    protected       UnderwritingRuleLink next;
    protected final String               ruleName;
    protected final RuleCategory         ruleCategory;

    public UnderwritingRuleLinkAbstract(String ruleName, RuleCategory ruleCategory) {
        this.ruleCategory = ruleCategory;
        this.ruleName = ruleName;
    }

    @Override
    public void setNext(UnderwritingRuleLink next) {
        this.next = next;
    }

    @Override
    public UnderwritingCase evaluate(UnderwritingCase underwritingCase) {
        RuleExecutionLog executionLog = RuleExecutionLog.builder()
                                        .ruleName(ruleName)
                                        .ruleCategory(ruleCategory)
                                        .build();
                                        
        boolean shouldShortCircuit = executeRule(underwritingCase, executionLog);
        executionLog.setExecutedAt(Instant.now());
        underwritingCase.getRuleExecutions().add(executionLog);

        if(!shouldShortCircuit && this.next != null) {
            return this.next.evaluate(underwritingCase);
        } else {
            return underwritingCase;
        }
    } 

    protected abstract boolean executeRule(UnderwritingCase underwritingCase, RuleExecutionLog executionLog);
    
}
