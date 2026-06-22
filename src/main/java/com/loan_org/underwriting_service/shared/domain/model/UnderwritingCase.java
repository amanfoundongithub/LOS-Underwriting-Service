package com.loan_org.underwriting_service.shared.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.loan_org.underwriting_service.features.bureau_enrichment.dto.BureauSnapshot;
import com.loan_org.underwriting_service.features.initialization.model.ApplicantSnapshot;
import com.loan_org.underwriting_service.features.rules_evaluation.dto.RuleExecutionLog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MongoDB document containing all the data pertaining to the underwriting case
 * for keeping record, as well as the track of all the underwriting lifetime for the
 * application.
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category MongoDB document
 */
@Document(collection = "underwriting_cases")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingCase {

    @Id
    private String id;

    // Application details below:

    @Indexed(unique = true)
    private String applicationId;

    @Indexed
    private String customerId;

    @Indexed(unique = true)
    private String correlationId;

    private ApplicantSnapshot applicantSnapshot;
    private String            strategyVersion;

    // UW execution results below:
    
    private BureauSnapshot         bureauSnapshot;
    private FinalAdjudication      finalAdjudication;
    @Builder.Default
    private List<RuleExecutionLog> ruleExecutions = new ArrayList<>();

    // Final verdict and status of lifetime:
    private UnderwritingStatus   status; 
    private UnderwritingRiskTier riskTier;

    // MongoDB audit of the document:

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
    
    @Version
    private Long    version;
}
