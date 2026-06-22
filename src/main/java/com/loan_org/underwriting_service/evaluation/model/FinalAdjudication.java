package com.loan_org.underwriting_service.evaluation.model;

import java.time.Instant;
import java.util.List;

public record FinalAdjudication(
    String decision,
    List<String> reasonCodes,
    String adjudicatorId, 
    String justificationNotes,
    Instant decidedAt
) {}
