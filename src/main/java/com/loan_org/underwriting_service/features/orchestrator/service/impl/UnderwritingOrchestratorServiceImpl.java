package com.loan_org.underwriting_service.features.orchestrator.service.impl;

import org.springframework.stereotype.Service;

import com.loan_org.underwriting_service.features.bureau_enrichment.dto.BureauSnapshot;
import com.loan_org.underwriting_service.features.bureau_enrichment.exception.BureauEnrichmentException;
import com.loan_org.underwriting_service.features.bureau_enrichment.service.BureauEnrichmentService;
import com.loan_org.underwriting_service.features.orchestrator.service.UnderwritingOrchestratorService;
import com.loan_org.underwriting_service.features.rules_evaluation.service.RulesEngineService;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingCase;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingStatus;
import com.loan_org.underwriting_service.shared.domain.repository.UnderwritingCaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnderwritingOrchestratorServiceImpl implements UnderwritingOrchestratorService {

    private final UnderwritingCaseRepository caseRepository;
    private final BureauEnrichmentService    bureauEnrichmentService;
    private final RulesEngineService         rulesEngineService;
    
    @Override
    public UnderwritingCase process(String applicationId) {
        UnderwritingCase underwritingCase = caseRepository.findByApplicationId(applicationId)
        .orElseThrow(() -> new RuntimeException());

        underwritingCase.setStatus(UnderwritingStatus.ENRICHMENT_STAGE);
        caseRepository.save(underwritingCase);

        try {
            BureauSnapshot snapshot = bureauEnrichmentService.fetchProfile(applicationId);
            underwritingCase.setBureauSnapshot(snapshot);
            caseRepository.save(underwritingCase);
        } catch(BureauEnrichmentException e) {
            underwritingCase.setStatus(UnderwritingStatus.ENRICHMENT_FAILED);
            return caseRepository.save(underwritingCase);
        }

        underwritingCase.setStatus(UnderwritingStatus.RULES_EVALUATION);
        return caseRepository.save(rulesEngineService.execute(underwritingCase));
    }
    
}
