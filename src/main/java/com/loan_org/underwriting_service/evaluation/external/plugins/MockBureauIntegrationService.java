package com.loan_org.underwriting_service.evaluation.external.plugins;

import java.time.Instant;
import java.util.Map;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.loan_org.underwriting_service.evaluation.external.BureauIntegrationService;
import com.loan_org.underwriting_service.evaluation.model.BureauSnapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MockBureauIntegrationService implements BureauIntegrationService {
    
    @Override
    public BureauSnapshot fetchProfile(String applicationId) {
        try {

            log.info("Fetching credit profile for applicationId: {}",
                applicationId
            );

            Map<String, Object> responseBody = Map.of(
                "bureauName", "EXPIRIAN",
                "creditScore", 750,
                "reportedOn", Instant.now().toString(),
                "activeAccountsCount", 3,
                "derogatoryMarks", 0
            );
            Document rawBsonResponse = new Document(responseBody);

            return BureauSnapshot.builder()
            .bureauName("EXPIRIAN")
            .creditScore(750)
            .fetchedAt(Instant.now())
            .rawResponse(rawBsonResponse)
            .build();

        } catch(Exception e) {
            log.error("Error during fetching of profile.", e);
            throw new RuntimeException();
        }
    }
    
}
