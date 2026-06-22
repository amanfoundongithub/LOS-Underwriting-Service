package com.loan_org.underwriting_service.shared.domain.model;

/**
 * Employment status allows for building profile for the person taking the 
 * loan. 
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category ENUM
 */
public enum EmploymentStatus {
    SALARIED,          // Corporate/Govt employees with a steady paycheck
    SELF_EMPLOYED,     // Business owners, traders, proprietors
    FREELANCER,        // Independent contractors, gig workers
    UNEMPLOYED,        // Currently without income from employment
    RETIRED            // Pensioners or individuals living off investments
}
