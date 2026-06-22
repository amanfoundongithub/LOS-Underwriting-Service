package com.loan_org.underwriting_service.evaluation.service.impl;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.loan_org.underwriting_service.evaluation.external.BureauIntegrationService;
import com.loan_org.underwriting_service.evaluation.model.ApplicantSnapshot;
import com.loan_org.underwriting_service.evaluation.model.BureauSnapshot;
import com.loan_org.underwriting_service.evaluation.model.FinalAdjudication;
import com.loan_org.underwriting_service.evaluation.model.RulesEngineExecutionResult;
import com.loan_org.underwriting_service.evaluation.model.UnderwritingCase;
import com.loan_org.underwriting_service.evaluation.repository.UnderwritingCaseRepository;
import com.loan_org.underwriting_service.evaluation.rules_engine.RulesEngine;
import com.loan_org.underwriting_service.evaluation.service.UnderwritingService;
import com.loan_org.underwriting_service.evaluation.web.dto.UnderwritingTriggerRequest;
import com.loan_org.underwriting_service.evaluation.web.dto.UnderwritingTriggerResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnderwritingServiceImpl implements UnderwritingService {

    private final UnderwritingCaseRepository repository;
    private final BureauIntegrationService   bureauIntegrationService;
    private final RulesEngine                rulesEngine;
    
    @Override
    public UnderwritingTriggerResponse create(UnderwritingTriggerRequest request) {

        log.info("Received request for triggering Underwriting Service for applicationId: {}.",
            request.applicationId()
        );
        
        // Search for application Id
        Optional<UnderwritingCase> foundCase = repository.findByApplicationId(
            request.applicationId()
        );
        if(foundCase.isPresent()) {
            log.warn("The received applicationId: {} already has a corresponding case registered." + 
            "Please contact the administrator for the same, if you think this is mistake. Aborting.", request.applicationId());
            
            // Generate a failed response to report to server
            return UnderwritingTriggerResponse.builder()
                    .caseId(foundCase.get().getId())
                    .status("ALREADY_EXISTS")
                    .message("The given applicationId already exists. Please do not try to generate more.")
                    .build();
        }

        // If not, we can start a new case

        // Build the application snapshot
        ApplicantSnapshot snapshot    = ApplicantSnapshot.builder()
                                        .declaredMonthlyIncome(request.declaredMonthlyIncome())
                                        .verifiedMonthlyIncome(request.verifiedMonthlyIncome())
                                        .employmentStatus(request.employmentStatus())
                                        .identityVerified(request.identityVerified())
                                        .build();

        // Build the main class                            
        UnderwritingCase newFreshCase = UnderwritingCase.builder()
                                        .applicationId(request.applicationId())
                                        .correlationId(request.correlationId())
                                        .status("INITIATED")
                                        .applicantSnapshot(snapshot)
                                        .build();
        
        // Save this to mongodb
        UnderwritingCase savedCase    = repository.save(newFreshCase);
        log.info("Successfully triggered Underwriting task for applicationId: {}", 
            savedCase.getApplicationId()
        );

        return UnderwritingTriggerResponse.builder()
                                          .caseId(savedCase.getId())
                                          .status(savedCase.getStatus())
                                          .message("Loan application successfully submitted for UNDERWRITING task.")
                                          .build();
    }

    @Override
    public UnderwritingCase process(String applicationId) {
        UnderwritingCase underwritingCase = repository.findByApplicationId(applicationId)
        .orElseThrow(() -> new RuntimeException());

        underwritingCase.setStatus("ENRICHMENT_STAGE");
        repository.save(underwritingCase);

        BureauSnapshot snapshot = bureauIntegrationService.fetchProfile(applicationId);
        underwritingCase.setBureauSnapshot(snapshot);
        repository.save(underwritingCase);

        underwritingCase.setStatus("RULES_EVALUATION");
        repository.save(underwritingCase);

        RulesEngineExecutionResult result = rulesEngine.execute(underwritingCase.getApplicantSnapshot(), snapshot);
        FinalAdjudication adjudication = new FinalAdjudication(
            "AUTO_" + result.status(),
            result.reasonCodes(),
            "SYSTEM_ENGINE",
            "Automated system scoring evaluation complete.",
            Instant.now()
        );

        underwritingCase.setRuleExecutions(result.ruleLogs());
        underwritingCase.setFinalAdjudication(adjudication);
        underwritingCase.setRiskTier(result.riskTier());
        underwritingCase.setStatus(result.status()); 

        return repository.save(underwritingCase);

    }

    @Override
    public Optional<UnderwritingCase> findByApplicationId(String applicationId) {
        return repository.findByApplicationId(applicationId);
    }

}
