package com.loan_org.underwriting_service.shared.domain.model;

/**
 * Refined list of statuses for the underwriting pipeline. 
 * These will help keep track of where the underwriting pipeline is 
 * currently situated at. 
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category ENUM
 */
public enum UnderwritingStatus {

    // ---- INITIALIZATION HELPERS ---=
    INITIATED,            // Successfully initiated the underwriting pipeline
    PROCESSING,           // Process queued/waiting for acceptance on the thread

    // ---- ENRICHMENT HELPERS ----
    ENRICHMENT_STAGE,     // Bureau Enrichment is being done
    ENRICHMENT_FAILED,    // Bureau Enrichment has failed

    // ---- RULES ENGINE HELPERS ---
    RULES_EVALUATION,     // Rules Engine is being run

    // ---- DECISION HELPERS ----
    APPROVED,             // Approved the loan for final proceedings
    DECLINED,             // Declined the loan for final proceedings
    EXPIRED,              // Expired decision

    // ---- MANUAL INTERVENTION ---
    REFERRED_TO_MANUAL,   // Manual intervention required

    // ---- OTHERS ----
    FAILED_SYSTEM_ERROR   // System failure occured; human intervention required

}
