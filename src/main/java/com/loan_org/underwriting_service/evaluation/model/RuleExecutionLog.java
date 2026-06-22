package com.loan_org.underwriting_service.evaluation.model;

import java.time.Instant;

public record RuleExecutionLog(
    String ruleName,
    String ruleCategory, // CREDIT, INCOME, FRAUD
    boolean passed,
    String observedValue,
    String thresholdValue,
    Instant executedAt
) {}
