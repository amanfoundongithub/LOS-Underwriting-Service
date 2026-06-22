package com.loan_org.underwriting_service.features.initialization.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.loan_org.underwriting_service.features.initialization.dto.UnderwritingTriggerRequest;
import com.loan_org.underwriting_service.features.initialization.dto.UnderwritingTriggerResponse;
import com.loan_org.underwriting_service.features.initialization.model.ApplicantSnapshot;
import com.loan_org.underwriting_service.features.initialization.service.UnderwritingInitializationService;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingCase;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingRiskTier;
import com.loan_org.underwriting_service.shared.domain.model.UnderwritingStatus;
import com.loan_org.underwriting_service.shared.domain.repository.UnderwritingCaseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnderwritingInitializationServiceImpl implements UnderwritingInitializationService {
    
    private final UnderwritingCaseRepository caseRepository;

    @Override
    public UnderwritingTriggerResponse initialize(UnderwritingTriggerRequest request) {
        log.info("Received request for triggering Underwriting Service for applicationId: {}, correlationId: {}",
            request.applicationId(), request.correlationId()
        );
        
        // Search for application Id
        Optional<UnderwritingCase> foundCase = caseRepository.findByApplicationId(
            request.applicationId()
        );

        if(foundCase.isPresent()) {
            UnderwritingCase existingCase = foundCase.get();
            log.info("ApplicationId: {} already has an active underwriting case ({}). Returning existing case metadata for idempotency.", 
                request.applicationId()
            );
            
            // Return the same result for idempotency
            return UnderwritingTriggerResponse.builder()
                    .caseId(existingCase.getId())
                    .applicationId(existingCase.getApplicationId())
                    .correlationId(existingCase.getCorrelationId())
                    .status(existingCase.getStatus())
                    .estimatedProcessingTimeMs(1500)
                    .message("Loan application was already submitted. Returning active underwriting case details.")
                    .build();
        }

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
                                        .status(UnderwritingStatus.INITIATED)
                                        .riskTier(UnderwritingRiskTier.UNASSIGNED)
                                        .applicantSnapshot(snapshot)
                                        .build();
        
        // Save this to mongodb
        UnderwritingCase savedCase    = caseRepository.save(newFreshCase);
        log.info("Successfully registered Underwriting task for applicationId: {} [Case ID: {}]", 
            savedCase.getApplicationId(), savedCase.getId()
        );

        return UnderwritingTriggerResponse.builder()
                                          .caseId(savedCase.getId())
                                          .applicationId(savedCase.getApplicationId())
                                          .correlationId(savedCase.getCorrelationId())
                                          .status(savedCase.getStatus())
                                          .estimatedProcessingTimeMs(1500)
                                          .message("Loan application successfully submitted for UNDERWRITING task.")
                                          .build();
    }

}
