package com.loan_org.underwriting_service.features.rules_evaluation.dto;

import java.time.Instant;

import com.loan_org.underwriting_service.features.rules_evaluation.model.RuleCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleExecutionLog{
    String ruleName;
    RuleCategory ruleCategory;
    boolean passed;
    String observedValue;
    String thresholdValue;
    Instant executedAt;
}
