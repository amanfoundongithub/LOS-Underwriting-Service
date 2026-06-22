package com.loan_org.underwriting_service.evaluation.model;

import java.util.List;

import lombok.Builder;

@Builder
public record RulesEngineExecutionResult(
    String status, 
    String riskTier, 
    List<RuleExecutionLog> ruleLogs, 
    List<String> reasonCodes
) {}