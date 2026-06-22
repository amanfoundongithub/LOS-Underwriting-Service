package com.loan_org.underwriting_service.evaluation.rules_engine.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.loan_org.underwriting_service.evaluation.model.ApplicantSnapshot;
import com.loan_org.underwriting_service.evaluation.model.BureauSnapshot;
import com.loan_org.underwriting_service.evaluation.model.RuleExecutionLog;
import com.loan_org.underwriting_service.evaluation.model.RulesEngineExecutionResult;
import com.loan_org.underwriting_service.evaluation.rules_engine.RulesEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MockRulesEngineImpl implements RulesEngine {

    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_DECLINED = "DECLINED";
    public static final String STATUS_REFERRED = "REFERRED_TO_MANUAL";

    @Override
    public RulesEngineExecutionResult execute(ApplicantSnapshot applicantSnapshot, BureauSnapshot bureauSnapshot) {
        List<RuleExecutionLog> logs = new ArrayList<>();
        List<String> reasonCodes = new ArrayList<>();

        // --- RULE 1: Minimum Credit Score Check ---
        int creditScore = bureauSnapshot.creditScore();
        boolean scorePass = creditScore >= 650;
        if (!scorePass) {
            reasonCodes.add("LOW_CREDIT_SCORE");
        }
        logs.add(new RuleExecutionLog(
            "MIN_CREDIT_SCORE_650", "CREDIT", scorePass, 
            String.valueOf(creditScore), "650", Instant.now()
        ));

        // --- RULE 2: Employment Verification Status ---
        boolean empPass = "SALARIED".equalsIgnoreCase(applicantSnapshot.employmentStatus()) || 
                          "SELF_EMPLOYED".equalsIgnoreCase(applicantSnapshot.employmentStatus());
        if (!empPass) {
            reasonCodes.add("INVALID_EMPLOYMENT_TYPE");
        }
        logs.add(new RuleExecutionLog(
            "VERIFY_EMPLOYMENT_TYPE", "INCOME", empPass, 
            applicantSnapshot.employmentStatus(), "SALARIED|SELF_EMPLOYED", Instant.now()
        ));

        // --- RULE 3: Basic Minimum Income Check ---
        double income = applicantSnapshot.verifiedMonthlyIncome();
        boolean incomePass = income >= 25000;
        if (!incomePass) {
            reasonCodes.add("INSUFFICIENT_INCOME");
        }
        logs.add(new RuleExecutionLog(
            "MIN_INCOME_25K", "INCOME", incomePass, 
            String.valueOf(income), "25000", Instant.now()
        ));

        // We look at critical failure points to assign the status and risk tiers
        String finalStatus;
        if (creditScore < 600 || !incomePass) {
            finalStatus = STATUS_DECLINED;
        } else if (creditScore >= 750 && scorePass && empPass && incomePass) {
            finalStatus = STATUS_APPROVED;
        } else {
            finalStatus = STATUS_REFERRED; // Borderline applications go to manual human review
        }

        String riskTier;
        if (creditScore >= 750) {
            riskTier = "TIER_A";
        } else if (creditScore >= 680) {
            riskTier = "TIER_B";
        } else if (creditScore >= 600) {
            riskTier = "TIER_C";
        } else {
            riskTier = "TIER_F";
        }

        return RulesEngineExecutionResult.builder()
        .status(finalStatus)
        .riskTier(riskTier)
        .reasonCodes(reasonCodes)
        .ruleLogs(logs)
        .build();
    }

}
