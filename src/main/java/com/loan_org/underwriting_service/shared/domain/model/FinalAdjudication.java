package com.loan_org.underwriting_service.shared.domain.model;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalAdjudication {
    private String decision;
    private List<String> reasonCodes;
    private String adjudicatorId;
    private String justificationNotes;
    private Instant decidedAt;
}
