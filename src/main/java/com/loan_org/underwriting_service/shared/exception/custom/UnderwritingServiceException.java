package com.loan_org.underwriting_service.shared.exception.custom;

/**
 * Defined as the generic exception that handles all the underwriting 
 * related exceptions as they occur during the processing of the loan.
 * 
 * This is the parent custom exception so that all exceptions can be grouped
 * under this exception itself.
 * 
 * @author amanfoundongithub
 * @version 1.0.0
 * 
 * @category Exception
 */
public class UnderwritingServiceException extends RuntimeException {
    
    public UnderwritingServiceException(RuntimeException e) {
        super(e);
    }

}
