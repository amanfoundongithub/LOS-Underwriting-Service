package com.loan_org.underwriting_service.features.bureau_enrichment.dto;

import java.time.Instant;
import org.bson.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BureauSnapshot {
    private String   bureauName;
    private int      creditScore;
    private Document rawResponse; 
    private Instant  fetchedAt;
}