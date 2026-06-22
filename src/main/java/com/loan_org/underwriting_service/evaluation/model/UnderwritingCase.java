package com.loan_org.underwriting_service.evaluation.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "underwriting_cases")
@Data
@Builder
public class UnderwritingCase {

    @Id
    private String id;

    @Indexed(unique = true)
    private String correlationId;

    @Indexed(unique = true)
    private String applicationId;

    private String status; 
    private String riskTier;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
    
    private ApplicantSnapshot applicantSnapshot;
    private BureauSnapshot bureauSnapshot;
    private List<RuleExecutionLog> ruleExecutions;
    private FinalAdjudication finalAdjudication;
    
}
