package com.loan_org.underwriting_service.evaluation.rules_engine;

import com.loan_org.underwriting_service.evaluation.model.ApplicantSnapshot;
import com.loan_org.underwriting_service.evaluation.model.BureauSnapshot;
import com.loan_org.underwriting_service.evaluation.model.RulesEngineExecutionResult;

public interface RulesEngine {
    RulesEngineExecutionResult execute(ApplicantSnapshot applicantSnapshot, BureauSnapshot bureauSnapshot);
}
