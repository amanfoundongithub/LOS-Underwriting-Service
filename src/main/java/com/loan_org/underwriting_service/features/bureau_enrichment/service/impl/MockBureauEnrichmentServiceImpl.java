package com.loan_org.underwriting_service.features.bureau_enrichment.service.impl;

import java.time.Instant;
import java.util.Map;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.loan_org.underwriting_service.features.bureau_enrichment.dto.BureauSnapshot;
import com.loan_org.underwriting_service.features.bureau_enrichment.exception.BureauEnrichmentException;
import com.loan_org.underwriting_service.features.bureau_enrichment.service.BureauEnrichmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MockBureauEnrichmentServiceImpl implements BureauEnrichmentService {

    @Override
    public BureauSnapshot fetchProfile(String applicationId) {

        log.info("Initiating external credit profile pull for applicationId: {}", 
            applicationId
        );

        try {
            
            Map<String, Object> responseBody = Map.of(
                "bureauName", "EXPERIAN",
                "creditScore", 750,
                "reportedOn", Instant.now().toString(),
                "activeAccountsCount", 3,
                "derogatoryMarks", 0
            );
            Document rawBsonResponse = new Document(responseBody);

            log.info("Successfully fetched credit profile for applicationId: {}. Score: {}", 
            applicationId, 750);

            return BureauSnapshot.builder()
                .bureauName("EXPERIAN")
                .creditScore(750)
                .fetchedAt(Instant.now())
                .rawResponse(rawBsonResponse)
                .build();

        } catch(Exception e) {
            log.error("Critical failure during external bureau network exchange for applicationId: {}", applicationId, e);
            throw new BureauEnrichmentException("Failed to fetch profile due to provider connection exception. Reason: " + e.getMessage());
        }
    }

    
}
