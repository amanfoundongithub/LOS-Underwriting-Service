package com.loan_org.underwriting_service.shared.domain.model;

/**
 * Risk tier results from the rules engine and bureau enrichment. This will be 
 * used to determine if the applicant can receive the loan or not.
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category ENUM
 */
public enum UnderwritingRiskTier {
    
    // ---- PRIME / SUPER PRIME TIEERS ----
    TIER_A,            // Super-prime (Lowest risk)
    TIER_B,            // Prime (Moderate-low risk)
    
    // ---- SUBPRIME TIERS ----
    TIER_C,            // Near-prime / Subprime (Moderate risk)
    TIER_D,            // High subprime (Significant risk)

    // ---- ALTERNATIVE DATA SEGMENTS ----
    NEW_TO_CREDIT,     // No traditional bureau history (Thin-file applicant)

    // ---- HARD DECLINE TIERS ----
    TIER_F,            // High risk category (Automatic credit decline)
    
    // ---- INITIALIZATION FALLBACK ----
    UNASSIGNED         // Evaluation has not run yet

}
