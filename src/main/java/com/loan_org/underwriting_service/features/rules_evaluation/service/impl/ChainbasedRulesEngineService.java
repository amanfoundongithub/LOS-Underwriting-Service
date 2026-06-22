package com.loan_org.underwriting_service.features.rules_evaluation.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.loan_org.underwriting_service.features.rules_evaluation.service.RulesEngineService;
import com.loan_org.underwriting_service.features.rules_evaluation.service.chain.UnderwritingRuleLink;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingCase;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingRiskTier;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingStatus;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChainbasedRulesEngineService implements RulesEngineService {

    private final List<UnderwritingRuleLink> availableRules;
    private UnderwritingRuleLink headOfChain;

    @PostConstruct
    public void assembleChain() {
        if (availableRules.isEmpty()) {
            throw new IllegalStateException("No underwriting rules detected in context configuration!");
        }
        
        // Dynamically wire the links together sequentially
        for (int i = 0; i < availableRules.size() - 1; i++) {
            availableRules.get(i).setNext(availableRules.get(i + 1));
        }
        // Save the execution entry point
        this.headOfChain = availableRules.get(0);
    }

    @Override
    public UnderwritingCase execute(UnderwritingCase underwritingCase) {
        
        // Default, feeling happy
        underwritingCase.setStatus(UnderwritingStatus.APPROVED);
        underwritingCase.setRiskTier(UnderwritingRiskTier.TIER_A);

        // Fire!
        return headOfChain.evaluate(underwritingCase);
    }
    
}
